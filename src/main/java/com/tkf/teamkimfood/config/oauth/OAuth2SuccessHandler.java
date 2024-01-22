package com.tkf.teamkimfood.config.oauth;

import com.tkf.teamkimfood.config.jwt.AuthTokens;
import com.tkf.teamkimfood.config.jwt.AuthTokensGenerator;
import com.tkf.teamkimfood.service.OAuthLoginService;
import com.tkf.teamkimfood.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    //public static final String REDIRECT_PATH = "/articles";

    private final OAuthLoginService oAuthLoginService;
    private final AuthTokensGenerator authTokensGenerator;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String)oAuth2User.getAttributes().get("email");
        String nickName = (String) oAuth2User.getAttributes().get("nickName");
        OAuthProvider oAuthProvider = OAuthProvider.KAKAO;

        OAuthInfoResponse oAuthInfoResponse = new OAuthInfoResponseImpl(email,nickName,oAuthProvider);

        Long memberId = oAuthLoginService.findOrCreateMember(oAuthInfoResponse);

        AuthTokens tokens = authTokensGenerator.generate(memberId);

    }

    //생성된 리프레시 토큰 쿠키에 저장
    private void addRefreshTokneToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken){
        int cookieMaxAge = (int)REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request,response,REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response,REFRESH_TOKEN_COOKIE_NAME,refreshToken,cookieMaxAge);
    }


}
