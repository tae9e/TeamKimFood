package com.tkf.teamkimfood.service;



import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.status.MemberRole;
import com.tkf.teamkimfood.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Service
@Transactional
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

       //권한 부여 (필요에 따라 주석 처리해서 사용하세요)
        List<GrantedAuthority> authorityList = new ArrayList<>();

//        if(member.getMemberRole().equals(MemberRole.ADMIN)){
//            authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        }

        if(member.getMemberRole().equals(MemberRole.USER)){
            authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

           return User.builder()
                   .username(member.getEmail())
                   .password(member.getPassword())
                   .authorities(authorityList)
                   .build();

    }
}
