package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.domain.oauth.OAuthInfoResponse;
import com.tkf.teamkimfood.domain.oauth.OAuthLoginParams;
import com.tkf.teamkimfood.domain.oauth.OAuthProvider;
import com.tkf.teamkimfood.domain.oauth.OauthApiClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

