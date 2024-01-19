package com.tkf.teamkimfood.domain.oauth;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickName();
    OAuthProvider getOAuthProvider();
}
