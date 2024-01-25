package com.tkf.teamkimfood.infra;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

public class CustomClientRegistration {

    public ClientRegistration kakaoClientRegistration(){
        return  ClientRegistration.withRegistrationId("kakao")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("profile_nickname","account_email")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .redirectUri("http://localhost:8080/oauth2/authorization/kakao")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .userNameAttributeName("id")
                .clientName("Kakao")
                .clientId("69445a05dee5a6928649b416c9df1964")
                .clientSecret("hxEg8JGhjQm7Np8bMd18vQWZhVOjcPm0")
                .build();

    }
}
