package com.tkf.teamkimfood.config.oauth;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OAuthInfoResponseImpl implements OAuthInfoResponse {

    private String email;
    private String nickName;
    private OAuthProvider oAuthProvider;

    public OAuthInfoResponseImpl(String email, String nickName, OAuthProvider oAuthProvider) {
        this.email = email;
        this.nickName = nickName;
        this.oAuthProvider = oAuthProvider;
    }

    @Override
    public String getEmail() {
       return email;
    }

    @Override
    public String getNickName() {
        return nickName;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return oAuthProvider;
    }
}
