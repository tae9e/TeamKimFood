package com.tkf.teamkimfood.config.oauth;

import com.tkf.teamkimfood.config.jwt.AuthTokens;
import com.tkf.teamkimfood.config.jwt.AuthTokensGenerator;

import com.tkf.teamkimfood.infra.KakaoInfoResponse;
import com.tkf.teamkimfood.service.OAuthLoginService;
import com.tkf.teamkimfood.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Log4j2

public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    private static final String REDIRECT_PATH = "/main";

    private final OAuthLoginService oAuthLoginService;
    private final AuthTokensGenerator authTokensGenerator;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        OAuthInfoResponse oAuthInfoResponse = extractOAuthInfo(oAuth2User);

        Long memberId = oAuthLoginService.findOrCreateMember(oAuthInfoResponse);
        AuthTokens tokens = authTokensGenerator.generate(memberId);

        addRefreshTokenToCookie(response, tokens.getRefreshToken());
        redirectStrategy.sendRedirect(request, response, REDIRECT_PATH);
    }
    private OAuthInfoResponse extractOAuthInfo(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = kakaoAccount != null ? (String)kakaoAccount.get("email"):null;
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String nickName = profile != null? (String) profile.get("nickname") : null;

        if(email == null && nickName == null){
            throw new IllegalArgumentException("존재하지 않는 회원정보입니다.");
        }
        OAuthProvider oAuthProvider = OAuthProvider.KAKAO;
        return new KakaoInfoResponse(email, nickName, oAuthProvider);
    }

    private void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }
}