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

        private final JwtTokenProvider jwtTokenProviderT;

        public AuthTokens generate(Long memberId) {
            long now = (new Date()).getTime();
            Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
            Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

            String subject = memberId.toString();
            String accessToken = jwtTokenProviderT.generate(subject, accessTokenExpiredAt);
            String refreshToken = jwtTokenProviderT.generate(subject, refreshTokenExpiredAt);

            return AuthTokens.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
        }

        public Long extractMemberId(String accessToken) {
            return Long.valueOf(jwtTokenProviderT.extractSubject(accessToken));
        }
    }

