package com.tkf.teamkimfood.infra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tkf.teamkimfood.config.oauth.OAuthInfoResponse;
import com.tkf.teamkimfood.config.oauth.OAuthProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

//현재 로그인한 사용자 정보 불러오기
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse extends OAuthInfoResponse {
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    public KakaoInfoResponse(
            String email, String nickName, OAuthProvider oAuthProvider) {
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        private String email;
        private KakaoProfile profile;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoProfile {
        private String nickname;
    }

    @Override
    public String getEmail() {
        return kakaoAccount != null ? kakaoAccount.email : null;
    }

    @Override
    public String getNickName() {
        return kakaoAccount != null && kakaoAccount.profile != null ? kakaoAccount.profile.nickname : null;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}