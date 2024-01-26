package com.tkf.teamkimfood.config;

import com.tkf.teamkimfood.config.jwt.AuthTokensGenerator;
import com.tkf.teamkimfood.config.jwt.JwtTokenProvider;
import com.tkf.teamkimfood.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.tkf.teamkimfood.config.oauth.OAuth2SuccessHandler;
import com.tkf.teamkimfood.infra.CustomClientRegistration;
import com.tkf.teamkimfood.service.OAuthLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@Log4j2
public class WebOauthSecurityConfig {

    private final AuthTokensGenerator authTokensGenerator;
    private final OAuthLoginService oAuthLoginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomClientRegistration customClientRegistration;
    @Bean
    //보안 설정
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(customizer -> customizer.disable()) // CSRF 보호 비활성화
                .authorizeRequests(authorizeRequests -> {
                    authorizeRequests
                            .requestMatchers(new AntPathRequestMatcher("/oauth2/authorization/kakao")).permitAll()
                            .requestMatchers("/public/**", "/login/**","/auth/kakao/callback").permitAll() // 특정 경로에 대한 접근 허용
                            .anyRequest().authenticated(); // 다른 모든 요청은 인증 필요
                })
                .httpBasic(Customizer.withDefaults())
                .sessionManagement((sessionManagement)->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(formLogincustomizer -> formLogincustomizer // 로그인 페이지 및 로그인 처리 URL 설정
                        .loginPage("/login/hello"))// 로그인 페이지 경로
                .oauth2Login(
                        oauth2 -> oauth2
                                .loginPage("/login/hello")
                                .authorizationEndpoint(authorizationEndpoint -> {
                                    authorizationEndpoint
                                            .baseUri("/oauth2/authorization")
                                            .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository());
                                })
                                .redirectionEndpoint(redirection->redirection.baseUri("oauth2/authorization/kakao")

                                )
                                .successHandler(oAuth2SuccessHandler()));
        return http.build();

    }

    //OAuth2 등록 정보 관리(Kakao OAuth 클라이언트 설정)
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(){
        return new InMemoryClientRegistrationRepository(customClientRegistration.kakaoClientRegistration());
    }


    //OAuth2 인증 클라이언트 정보 저장, 관리
    @Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository(OAuth2AuthorizedClientService authorizedClientService){
        return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
    }

    //인증 처리
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(jwtTokenProvider);
    }

    //인증 요청 쿠키에 저장
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    //비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler(){
        return new OAuth2SuccessHandler(oAuthLoginService,
                authTokensGenerator);
    }


    }

