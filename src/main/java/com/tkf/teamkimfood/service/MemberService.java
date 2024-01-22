package com.tkf.teamkimfood.service;


import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember.isPresent()){
            throw new IllegalStateException("이미 가입된 회원입니다");
        }


    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       Member member = memberRepository.findByEmail(email)
               .orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

           return User.builder()
                   .username(member.getEmail())
                   .password(member.getPassword())
                   .authorities("")
                   .build();

    }
}
