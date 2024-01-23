package com.tkf.teamkimfood.config.oauth;

//사용자 정보 저장
public interface OAuthInfoResponse {
    String getEmail();
    String getNickName();
    OAuthProvider getOAuthProvider();
}
