package com.tkf.teamkimfood.infra;

import com.tkf.teamkimfood.config.oauth.OAuthInfoResponse;
import com.tkf.teamkimfood.config.oauth.OAuthLoginParams;
import com.tkf.teamkimfood.config.oauth.OAuthProvider;
import com.tkf.teamkimfood.config.oauth.OauthApiClient;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

//Kakao Api와의 연결 및 요청 처리
@Component
@RequiredArgsConstructor
@Setter
@Log4j2
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

    private final RestTemplate restTemplate;


    @Override
    public OAuthProvider oAuthProvider() {

        return OAuthProvider.KAKAO;
    }

    //Kakao로부터 Access토큰 요청
    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        String url = authUrl + "/oauth/token";
        log.info("url?{}" + url);

       HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("redirect_url",redirectUrl);

        log.info("request{}: " + body);
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        KakaoTokens response = restTemplate.postForObject(url, request, KakaoTokens.class);
        log.info("response{}:" + response);

        assert response != null;
        return response.getAccessToken();
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
