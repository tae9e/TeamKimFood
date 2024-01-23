package com.tkf.teamkimfood.config.oauth;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
//Oauth 로그인 요청을 위한 메소드 생성
@Service
public interface OAuthLoginParams {

    OAuthProvider oAuthProvider();

    //OAuth 로그인 요청을 위한 HTTP 요청 바디를 생성
    MultiValueMap<String, String> makeBody();

}
