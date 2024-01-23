package com.tkf.teamkimfood.config;

import com.tkf.teamkimfood.config.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

    @RequiredArgsConstructor
    public class TokenAuthenticationFilter extends OncePerRequestFilter {
        private final JwtTokenProvider jwtTokenProvider;

        private final static String HEADER_AUTHORIZATION = "Authorization";
        private final static String TOKEN_PREFIX = "Bearer ";

        @Override
        protected void doFilterInternal(
                HttpServletRequest request,
                HttpServletResponse response,
                FilterChain filterChain)  throws ServletException, IOException {
            //요청 헤더의 Authorization키의 값 조회
            String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
            //가져온 접두사 제거
            String token = getAccessToken(authorizationHeader);
            //유효성 접근 => 인증 정보 설정
            if (jwtTokenProvider.validToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        }

        private String getAccessToken(String authorizationHeader) {
            //Token유효성 확인
            if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
                return authorizationHeader.substring(TOKEN_PREFIX.length());
            }

            return null;
        }
    }

}
