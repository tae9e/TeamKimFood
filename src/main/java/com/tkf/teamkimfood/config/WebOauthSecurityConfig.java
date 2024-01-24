package com.tkf.teamkimfood.config;

import com.tkf.teamkimfood.config.jwt.AuthTokensGenerator;
import com.tkf.teamkimfood.config.jwt.JwtTokenProvider;
import com.tkf.teamkimfood.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.tkf.teamkimfood.config.oauth.OAuth2SuccessHandler;
import com.tkf.teamkimfood.service.OAuth2UserCustomService;
import com.tkf.teamkimfood.service.OAuthLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@Log4j2
public class WebOauthSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthTokensGenerator authTokensGenerator;
    private final OAuthLoginService oAuthLoginService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(customizer -> customizer.disable()) // CSRF 보호 비활성화
                .authorizeRequests(authorizeRequests -> {
                    authorizeRequests
                            .requestMatchers(new AntPathRequestMatcher("/auth/kakao/callback")).permitAll()
                            .requestMatchers("/public/**", "/login/**").permitAll() // 특정 경로에 대한 접근 허용
                            .anyRequest().authenticated(); // 다른 모든 요청은 인증 필요
                })
//                .formLogin(formLogincustomizer->formLogincustomizer // 로그인 페이지 및 로그인 처리 URL 설정
//                .loginPage("/login") // 로그인 페이지 경로
//                .loginProcessingUrl("/perform_login") // 로그인 처리 URL 경로
//                .defaultSuccessUrl("/dashboard") // 로그인 성공 후 이동할 페이지
//                .permitAll()) // 로그인 페이지는 누구나 접근 가능
//                .logout(formLogoutcustomizer->formLogoutcustomizer // 로그아웃 설정
//                .logoutUrl("/perform_logout") // 로그아웃 처리 URL 경로
//                .logoutSuccessUrl("/login") // 로그아웃 성공 후 이동할 페이지
//                .permitAll()); // 로그아웃 페이지는 누구나 접근 가능
                .oauth2Login(
                        oauth2 -> oauth2
                       // .loginPage("/login")
                        .authorizationEndpoint(authorizationEndpoint -> {
                            authorizationEndpoint
                                    .baseUri("/oauth2/authorization");
                        })
                        .successHandler(oAuth2SuccessHandler())
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserCustomService)
                ));
        return http.build();
    }

//                .formLogin(formLogincustomizer->formLogincustomizer // 로그인 페이지 및 로그인 처리 URL 설정
//                .loginPage("/login") // 로그인 페이지 경로
//                .loginProcessingUrl("/perform_login") // 로그인 처리 URL 경로
//                .defaultSuccessUrl("/dashboard") // 로그인 성공 후 이동할 페이지
//                .permitAll()) // 로그인 페이지는 누구나 접근 가능
//                .logout(formLogoutcustomizer->formLogoutcustomizer // 로그아웃 설정
//                .logoutUrl("/perform_logout") // 로그아웃 처리 URL 경로
//                .logoutSuccessUrl("/login") // 로그아웃 성공 후 이동할 페이지
//                .permitAll()); // 로그아웃 페이지는 누구나 접근 가능



    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(
                oAuthLoginService,
                authTokensGenerator,
                oAuth2AuthorizationRequestBasedOnCookieRepository());
    }


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(){
        return new InMemoryClientRegistrationRepository(this.kakaoClientRegistration());
    }


    @Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository(OAuth2AuthorizedClientService authorizedClientService){
        return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
    }

    private ClientRegistration kakaoClientRegistration(){
        ClientRegistration.Builder builder=ClientRegistration.withRegistrationId("kakao");
        builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
        builder.scope(new String[]{"profile_nickname","account_email"});
        builder.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST);
        builder.authorizationUri("https://kauth.kakao.com/oauth/authorize");
        builder.redirectUri("http://localhost:8080/auth/kakao/callback");
        builder.tokenUri("https://kauth.kakao.com/oauth/token");
        builder.userInfoUri("https://kapi.kakao.com/v2/user/me");
        builder.userNameAttributeName("id");
        builder.clientName("Kakao");
        builder.clientId("69445a05dee5a6928649b416c9df1964");
        builder.clientSecret("hxEg8JGhjQm7Np8bMd18vQWZhVOjcPm0");
        return builder.build();
    }




    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }
}


