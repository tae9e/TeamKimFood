package com.tkf.teamkimfood.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
@Configuration
public class CustomClientRegistration {

    @Value("${oauth.kakao.api-url}")
    private String apiUrl;

    @Value("${oauth.kakao.oauth-url}")
    private String authUrl;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUrl;

    @Value("${oauth.kakao.client-secret")
    private String clientSecret;

    @Bean
    public ClientRegistration kakaoClientRegistration(){
        return ClientRegistration.withRegistrationId("kakao")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("profile_nickname", "account_email")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationUri(apiUrl + "/authorize")
                .redirectUri(redirectUrl)
                .tokenUri(authUrl + "/token")
                .userInfoUri(apiUrl + "/v2/user/me")
                .userNameAttributeName("id")
                .clientName("Kakao")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

    }
}
