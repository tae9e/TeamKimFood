package com.tkf.teamkimfood.controller;

import com.tkf.teamkimfood.config.oauth.OAuthInfoResponse;
import com.tkf.teamkimfood.infra.KakaoApiClient;
import com.tkf.teamkimfood.infra.KakaoLoginParams;
import com.tkf.teamkimfood.service.OAuthLoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@Slf4j
@RequestMapping("/public")
public class OauthController {

    private final OAuthLoginService oAuthLoginService;
    private final KakaoApiClient kakaoApiClient;



    public OauthController(OAuthLoginService oAuthLoginService,KakaoApiClient kakaoApiClient) {
        this.kakaoApiClient=kakaoApiClient;
        this.oAuthLoginService = oAuthLoginService;
    }

    @GetMapping("/auth/kakao/login")
    public void KakaoOauthTest(HttpServletResponse response) throws IOException {
        response.sendRedirect(kakaoApiClient.getAuthorizeUrl());
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(@RequestParam("code") String code) {
        KakaoLoginParams kakaoLoginParams=new KakaoLoginParams();
        log.info("KakaoParams : {}", kakaoLoginParams);
       //로그는 (+) 쓰지말고 아래처럼 "" 안에는 {} 로 변수 위치 잡아주고 (,) 뒤에다가 해당 위치에 넣을 변수 지정해주면 됨
        log.info("code : {}", code);
        kakaoLoginParams.setAuthorizationCode(code);

        String accessToken = kakaoApiClient.requestAccessToken(kakaoLoginParams);
        OAuthInfoResponse userInfo = kakaoApiClient.requestOauthInfo(accessToken);
        log.info("{}", userInfo.getEmail());
        // 여기서 리다이렉트 하지말고 토큰 값이랑 프론트에서 필요한 사용자 정보를 보내주면 됨
        return "redirect:/login/success";
    }

}
