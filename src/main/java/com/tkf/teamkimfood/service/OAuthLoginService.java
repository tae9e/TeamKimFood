package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.config.jwt.AuthTokens;
import com.tkf.teamkimfood.config.jwt.AuthTokensGenerator;
import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.config.oauth.OAuthInfoResponse;
import com.tkf.teamkimfood.config.oauth.OAuthLoginParams;
import com.tkf.teamkimfood.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class OAuthLoginService {
    private final MemberRepository memberRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;


    public AuthTokens login(OAuthLoginParams params){
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        log.info("oAuthInfoResponse{}: " + oAuthInfoResponse);
        Long memberId = findOrCreateMember(oAuthInfoResponse);
        log.info("memberId{}: " +memberId);
        return authTokensGenerator.generate(memberId);
    }

    //사용자를 찾거나 새로 생성
    public Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse){
        return memberRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(Member::getId)
                .orElseGet(() -> newMember(oAuthInfoResponse));
    }

    //새로운 사용자 생성
    private Long newMember(OAuthInfoResponse oAuthInfoResponse){
        Member member = Member.builder()
                .email(oAuthInfoResponse.getEmail())
                .nickname(oAuthInfoResponse.getNickName())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .build();


                return memberRepository.save(member).getId();
    }




}
