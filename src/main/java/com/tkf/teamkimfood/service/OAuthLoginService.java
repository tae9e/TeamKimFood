package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.config.jwt.AuthTokens;
import com.tkf.teamkimfood.config.jwt.AuthTokensGenerator;
import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.oauth.OAuthInfoResponse;
import com.tkf.teamkimfood.domain.oauth.OAuthLoginParams;
import com.tkf.teamkimfood.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final MemberRepository memberRepository;
    private final AuthTokensGenerator authTokensGeneratorT;
    private final RequestOAuthInfoService requestOAuthInfoServiceT;

    public AuthTokens login(OAuthLoginParams params){
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoServiceT.request(params);
        Long memberId = findOrCreateMember(oAuthInfoResponse);
        return authTokensGeneratorT.generate(memberId);
    }

    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse){
        return memberRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(Member::getId)
                .orElseGet(() -> newMember(oAuthInfoResponse));
    }

    private Long newMember(OAuthInfoResponse oAuthInfoResponse){
        Member member = Member.builder()
                .email(oAuthInfoResponse.getEmail())
                .nickname(oAuthInfoResponse.getNickName())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .build();


                return memberRepository.save(member).getId();
    }




}
