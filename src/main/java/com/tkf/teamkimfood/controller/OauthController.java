package com.tkf.teamkimfood.controller;

import com.tkf.teamkimfood.config.jwt.AuthTokens;
import com.tkf.teamkimfood.infra.KakaoLoginParams;
import com.tkf.teamkimfood.service.OAuthLoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@Controller
@Log4j2
@RequestMapping("/public")
public class OauthController {

    @GetMapping("/login")
    public String auth(){
        return "main";
    }


    @GetMapping("/auth/loginForm")
    public String KakaoOauth(){

        return "logintest/kakaoLogin";
    }


   @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(@RequestParam String code){
       RestTemplate restTemplate = new RestTemplate();
       HttpHeaders headers = new HttpHeaders();
       headers.add("Content-type","application/x-www-form-urlencode;charset=utf-8");
       MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
       params.add("grant_type","authorization_code");
       params.add("client_id","69445a05dee5a6928649b416c9df1964");
       params.add("redirect_uri","http://localhost:8080/auth/kakao/callback");
       params.add("code",code);

       log.info("토큰 요청" + code);

       HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params,headers);

       ResponseEntity response = restTemplate.exchange("https://kauth.kakao.com/oauth/token,",
               HttpMethod.POST,
               kakaoTokenRequest,
               String.class);
       return "카카오 토큰 요청, 토큰 요청에 대한 응답" + response;

   }




}
