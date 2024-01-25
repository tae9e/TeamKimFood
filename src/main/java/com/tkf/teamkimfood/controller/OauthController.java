package com.tkf.teamkimfood.controller;

import com.tkf.teamkimfood.config.jwt.AuthTokens;
import com.tkf.teamkimfood.config.oauth.OAuthInfoResponse;
import com.tkf.teamkimfood.config.oauth.OAuthLoginParams;
import com.tkf.teamkimfood.infra.KakaoApiClient;
import com.tkf.teamkimfood.infra.KakaoLoginParams;
import com.tkf.teamkimfood.service.OAuthLoginService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;



@Controller
@Log4j2
@RequestMapping("/public")
public class OauthController {

    private final OAuthLoginService oAuthLoginService;
    private final KakaoApiClient kakaoApiClient;



    public OauthController(OAuthLoginService oAuthLoginService,KakaoApiClient kakaoApiClient) {
        this.kakaoApiClient=kakaoApiClient;
        this.oAuthLoginService = oAuthLoginService;
    }

//    @PostMapping("/kakao")
//    public ResponseEntity<AuthTokens> loginKakao(@RequestBody KakaoLoginParams params){
//        return ResponseEntity.ok(oAuthLoginService.login(params));
//    }

    @GetMapping("/auth/loginForm")
    public String KakaoOauth(){

        return "logintest/kakaoLogin";
    }


    @PostMapping("/auth/kakao/callback")
    public String kakaoCallback(@RequestParam String code) {
        KakaoLoginParams kakaoLoginParams=new KakaoLoginParams();
        log.info("KakaoParams{}: " + kakaoLoginParams);
        log.info("code?{}"+code);
        kakaoLoginParams.setAuthorizationCode(code);

        String accessToken = kakaoApiClient.requestAccessToken(kakaoLoginParams);
        OAuthInfoResponse userInfo = kakaoApiClient.requestOauthInfo(accessToken);

        return "카카오 로그인 처리 완료";
    }

}
