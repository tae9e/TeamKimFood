package com.tkf.teamkimfood.config.oauth;

import com.tkf.teamkimfood.config.jwt.AuthTokens;
import com.tkf.teamkimfood.config.jwt.AuthTokensGenerator;
import com.tkf.teamkimfood.config.jwt.JwtTokenProvider;

import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.RefreshToken;
import com.tkf.teamkimfood.domain.User;
import com.tkf.teamkimfood.repository.MemberRepository;
import com.tkf.teamkimfood.repository.RefreshTokenRespository;
import com.tkf.teamkimfood.service.OAuthLoginService;
import com.tkf.teamkimfood.service.UserService;
import com.tkf.teamkimfood.util.CookieUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler  {




    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final String REDIRECT_PATH = "auth/kakao/callback";

    private final RefreshTokenRespository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = userService.findByEmail((String)oAuth2User.getAttributes().get("email"));

        String subject = user.getMember().getEmail();

        Date refreshTokenExpiry = new Date(System.currentTimeMillis()+REFRESH_TOKEN_DURATION.toMillis());
        Date accessTokenExpiry = new Date(System.currentTimeMillis() + ACCESS_TOKEN_DURATION.toMillis());

        String refreshToken=jwtTokenProvider.generate(subject,refreshTokenExpiry);
        saveRefreshToken(user.getId(),refreshToken);

        String accessToken = jwtTokenProvider.generate(subject,accessTokenExpiry);
        String targetUrl = getTargetUrl(accessToken);
        getRedirectStrategy().sendRedirect(request,response,targetUrl);

    }

    private void saveRefreshToken(Long memberId,String newRefreshToken){
        Member member = memberRepository.findById(memberId).orElseThrow(()->new EntityNotFoundException("Member not found"));
        RefreshToken refreshToken=refreshTokenRepository.findByMember(member)
                .map(entity->entity.update(newRefreshToken))
                .orElseGet(() -> new RefreshToken(member, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }


    //생성된 리프레시 토큰 쿠키에 저장
    private void addRefreshTokneToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken){
        int cookieMaxAge = (int)REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request,response,REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response,REFRESH_TOKEN_COOKIE_NAME,refreshToken,cookieMaxAge);
    }

    //인증 요청과 관련된 속성 삭제
//    private void clearAuthenticationAttributes(HttpServletRequest request,HttpServletResponse response){
//        super.clearAuthenticationAttributes(request);
//        authorizationRequestRepository.removeAuthorizationRequestCookies(request,response);
//
//    }


    //리다이렉트할 url 생성
    private String getTargetUrl(String token){
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token",token)
                .build()
                .toUriString();
    }



}
