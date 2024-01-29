package com.tkf.teamkimfood.infra;

import com.tkf.teamkimfood.config.oauth.OAuthInfoResponse;
import com.tkf.teamkimfood.config.oauth.OAuthLoginParams;
import com.tkf.teamkimfood.config.oauth.OAuthProvider;
import com.tkf.teamkimfood.config.oauth.OauthApiClient;
import com.tkf.teamkimfood.domain.KakaoUserInfo;
import com.tkf.teamkimfood.dto.KakaoMemberDto;
import com.tkf.teamkimfood.repository.KakaoUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Kakao Api와의 연결 및 요청 처리
 */
@Component
@RequiredArgsConstructor
@Setter
@Slf4j
public class KakaoApiClient implements OauthApiClient {

    private static final String GRANT_TYPE="authorization_code";

    @Value("${oauth.kakao.api-url}")
    private String apiUrl;

    @Value("${oauth.kakao.oauth-url}")
    private String authUrl;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUrl;

    @Value("${oauth.kakao.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;


    @Override
    public OAuthProvider initOAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String getAuthorizeUrl() {
        String url = authUrl + "/oauth/authorize?"
                + "client_id=" + clientId
                + "&redirect_uri=" + redirectUrl
                + "&response_type=code";

        return url;
    }

    //Kakao로부터 Access토큰 요청
    @Override
    public String requestAccessToken(OAuthLoginParams params) {
      try {
          String url = authUrl + "/oauth/token";
          log.info("url : {}", url);

          HttpHeaders httpHeaders = new HttpHeaders();
          httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

          String credentialSecret = clientId + ":" + clientSecret;
          String base64Credentials = Base64.getEncoder().encodeToString(credentialSecret.getBytes());
          httpHeaders.add("Authorization", "Basic " + base64Credentials);


          MultiValueMap<String, String> body = params.makeBody();
          body.add("grant_type", GRANT_TYPE);
          body.add("client_id", clientId);
          body.add("redirect_uri", redirectUrl);
          body.add("client_secret", clientSecret);

          log.info("client_id: {}", clientId);
          log.info("redirect_uri: {}", redirectUrl);


          log.info("request: {}", body);
          HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

          KakaoTokens response = restTemplate.postForObject(url, request, KakaoTokens.class);
          log.info("response : {}", response);


          return response != null ? response.getAccessToken() : null;
      }catch(RestClientException e){
          return null;
      }

    }

    //액세스 토큰을 이용해 사용자 정보 요청
    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        String url = apiUrl + "/v2/user/me";
        log.info("apiUrl url{}" + apiUrl);

        HttpHeaders httpHeaders = new HttpHeaders();
        log.info("httpHeaders{}: " + httpHeaders);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");
        log.info("body{}" + body);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        log.info("request{}" + request);

        return restTemplate.postForObject(url, request, KakaoInfoResponse.class);
    }




}
