package com.tkf.teamkimfood.service;



import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.prefer.RecipeCategory;
import com.tkf.teamkimfood.domain.status.MemberRole;

import com.tkf.teamkimfood.dto.MemberFormDto;
import com.tkf.teamkimfood.dto.aboutrecipe.RecipeSearchDto;
import com.tkf.teamkimfood.repository.MemberRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Service
@Transactional
@RequiredArgsConstructor
@Getter
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecipeCategoryRepository recipeCategoryRepository;

    //중복 회원 검사해서 저장
    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    //유효성 검증, 존재하는 회원일 경우 예외 발생
    private void validateDuplicateMember(Member member){
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember.isPresent()){
            throw new IllegalStateException("이미 가입된 회원입니다");
        }


    }

    //이메일을 이용해 회원 정보 조회
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.tkf.teamkimfood.domain.Member member = memberRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

       //권한 부여 (필요에 따라 주석 처리해서 사용하세요)
        List<GrantedAuthority> authorityList = new ArrayList<>();


        if(member.getMemberRole().equals(MemberRole.USER)){
            authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        }else if(member.getMemberRole().equals(MemberRole.ADMIN))
            authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        User user = new User(
                member.getEmail(),
                member.getPassword(),
                authorityList
        );
        return user;

    }

    //회원 정보 불러오기
    public Optional<Member> getMemberId(Long memberId){
        Optional<Member> member = memberRepository.findById(memberId);
        return member;
    }

    //회원 수정
    public Long updateMember(MemberFormDto memberFormDto){
        Member member = memberRepository.findByEmail(memberFormDto.getEmail()).orElseThrow(EntityNotFoundException::new);

        member.setName(memberFormDto.getName());
        member.setNickname(memberFormDto.getNickname());
        member.setEmail(memberFormDto.getEmail());
        member.setPassword(memberFormDto.getPassword());
        member.setPhoneNumber(memberFormDto.getPhoneNumber());

        memberRepository.save(member);
        return member.getId();
    }

    //회원 삭제
    public void deleteMember(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);
        memberRepository.delete(member);
    }

    //로그인을위한 멤버 불러오기 -김원성
    public Long findMemberForLogin(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow();
        if (member != null && passwordEncoder.matches(password, member.getPassword())) {
            return member.getId();
        } else {
            throw new BadCredentialsException("유저이름 혹은 비밀번호가 다릅니다.");
        }
    }
    public Long findById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Member with email " + id + " not found"));
        List<RecipeCategory> byMember = recipeCategoryRepository.findByMember(member);
        if (byMember == null) {
            return null;
        }else{

            return member.getId();
        }
    }
}
