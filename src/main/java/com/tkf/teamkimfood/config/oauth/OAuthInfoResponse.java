package com.tkf.teamkimfood.config.oauth;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickName();
    OAuthProvider getOAuthProvider();
}
