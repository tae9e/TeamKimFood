package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.config.oauth.OAuthInfoResponse;
import com.tkf.teamkimfood.config.oauth.OAuthLoginParams;
import com.tkf.teamkimfood.config.oauth.OAuthProvider;
import com.tkf.teamkimfood.config.oauth.OauthApiClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
//인증 서버로부터 사용자 정보를 받아옴
@Component
public class RequestOAuthInfoService {
    private final Map<OAuthProvider, OauthApiClient> clients;

    public RequestOAuthInfoService(List<OauthApiClient> clients) {
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OauthApiClient::oAuthProvider, Function.identity())
        );
    }

    public OAuthInfoResponse request(OAuthLoginParams params){
        OauthApiClient client = clients.get(params.oAuthProvider());
        String accessToken = client.requestAccessToken(params);
        return client.requestOauthInfo(accessToken);
    }
}

