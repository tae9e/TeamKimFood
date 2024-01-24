package com.tkf.teamkimfood.config.oauth;

//외부 API와의 통신 관리
public interface OauthApiClient {
    OAuthProvider oAuthProvider();
    String requestAccessToken(OAuthLoginParams params);
    OAuthInfoResponse requestOauthInfo(String accessToken);

}
