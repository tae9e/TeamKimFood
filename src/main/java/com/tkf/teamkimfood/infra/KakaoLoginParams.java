package com.tkf.teamkimfood.infra;

import com.tkf.teamkimfood.config.oauth.OAuthLoginParams;
import com.tkf.teamkimfood.config.oauth.OAuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
@Setter
@RequiredArgsConstructor
public class KakaoLoginParams implements OAuthLoginParams {
        private String authorizationCode;


    @Override
    public OAuthProvider oAuthProvider() {

        return OAuthProvider.KAKAO;
    }

    //OAuth 인증 요청에 필요한 파라미터 생성
    @Override
        public MultiValueMap<String, String> makeBody() {
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("code", authorizationCode);
            return body;
        }
    }

