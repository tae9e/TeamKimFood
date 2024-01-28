package com.tkf.teamkimfood.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor

public class AuthTokensGenerator {
        //인증 타입으로 JWT 토큰 사용
        private static final String BEARER_TYPE = "Bearer";
        private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // Access Token 유효 기간 30분
        private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // Refrech Token 유효 기간 7일

        private final JwtTokenProvider jwtTokenProvider;


        //사용자 아이디에 대한 토큰 생성
        public AuthTokens generate(Long memberId){
            String accessToken = createAccessToken(memberId);
            String refreshToken = createRefreshToken(memberId);
            return AuthTokens.create(accessToken,refreshToken,BEARER_TYPE,ACCESS_TOKEN_EXPIRE_TIME);
        }


        private String createAccessToken(Long memberId){
            long now = (new Date()).getTime();
            Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
            String subject = memberId.toString();
            return jwtTokenProvider.generate(subject,accessTokenExpiredAt);

        }

        private String createRefreshToken(Long memberId){
            long now = (new Date()).getTime();
            Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
            String subject = memberId.toString();

            return jwtTokenProvider.generate(subject,refreshTokenExpiredAt);
        }

    }

