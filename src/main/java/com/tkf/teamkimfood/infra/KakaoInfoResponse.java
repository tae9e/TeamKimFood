package com.tkf.teamkimfood.infra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tkf.teamkimfood.config.oauth.OAuthInfoResponse;
import com.tkf.teamkimfood.config.oauth.OAuthProvider;
import lombok.Getter;
//현재 로그인한 사용자 정보 불러오기
public class KakaoInfoResponse implements OAuthInfoResponse {
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        private KakaoProfile profile;
        private String email;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoProfile {
        private String nickname;
    }

    @Override
    public String getEmail() {

        return kakaoAccount.email;
    }

    @Override
    public String getNickName() {

        return kakaoAccount.profile.nickname;
    }



    @Override
    public OAuthProvider getOAuthProvider() {

        return OAuthProvider.KAKAO;
    }
}
