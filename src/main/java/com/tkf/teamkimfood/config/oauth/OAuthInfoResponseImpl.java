package com.tkf.teamkimfood.config.oauth;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OAuthInfoResponseImpl implements OAuthInfoResponse{
    private final String email;
    private final String nickName;
    private final OAuthProvider oAuthProvider;


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
