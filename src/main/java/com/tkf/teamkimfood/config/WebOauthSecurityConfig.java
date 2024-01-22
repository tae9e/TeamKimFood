package com.tkf.teamkimfood.config;

import com.tkf.teamkimfood.config.jwt.JwtTokenProvider;
import com.tkf.teamkimfood.config.oauth.OAuth2SuccessHandler;
import com.tkf.teamkimfood.repository.query.RefreshTokenRespository;
import com.tkf.teamkimfood.service.OAuthLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@Log4j2
public class WebOauthSecurityConfig {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtTokenProvider jwtTokenProvider;
//    private final RefreshTokenRespository refreshTokenRepository;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(customizer -> customizer.disable()) // CSRF 보호 비활성화
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/public/**","/login/**").permitAll() // 특정 경로에 대한 접근 허용
                                .anyRequest().authenticated() // 다른 모든 요청은 인증 필요
                );
//                .formLogin(formLogincustomizer->formLogincustomizer // 로그인 페이지 및 로그인 처리 URL 설정
//                .loginPage("/login") // 로그인 페이지 경로
//                .loginProcessingUrl("/perform_login") // 로그인 처리 URL 경로
//                .defaultSuccessUrl("/dashboard") // 로그인 성공 후 이동할 페이지
//                .permitAll()) // 로그인 페이지는 누구나 접근 가능
//                .logout(formLogoutcustomizer->formLogoutcustomizer // 로그아웃 설정
//                .logoutUrl("/perform_logout") // 로그아웃 처리 URL 경로
//                .logoutSuccessUrl("/login") // 로그아웃 성공 후 이동할 페이지
//                .permitAll()); // 로그아웃 페이지는 누구나 접근 가능

        return http.build();
    }
}

//    @Bean
//    public OAuth2SuccessHandler oAuth2SuccessHandler() {
//        return new OAuth2SuccessHandler(jwtTokenProvider,
//                refreshTokenRepository,
//                oAuth2AuthorizationRequestBasedOnCookieRepository(),
//                userService);
//    }
//
//    @Bean
//    public TokenAuthenticationFilter tokenAuthenticationFilter() {
//        return new TokenAuthenticationFilter(jwtTokenProvider);
//    }
//
//    @Bean
//    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
//        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
//    }

