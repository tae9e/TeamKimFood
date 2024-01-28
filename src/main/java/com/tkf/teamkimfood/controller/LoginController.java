package com.tkf.teamkimfood.controller;


import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.dto.MemberDto;
import com.tkf.teamkimfood.dto.MemberFormDto;
import com.tkf.teamkimfood.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor

public class LoginController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/member")
    public String loginMember(Model model){
    model.addAttribute("memberFormDto",new MemberFormDto());
    return "login/memberLogin";
    }

    @PostMapping("/member")
    public String createMember(@Valid MemberFormDto memberFormDto , BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "login/memberLogin";
        }

        try{
            Member member = Member.createMember(memberFormDto,passwordEncoder);
            memberService.saveMember(member);
            return "login/success";
        }catch(IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "login/memberLogin";
        }

    }

    //회원 수정
    @PutMapping("/member/udpate/{memberId}")
    public @ResponseBody ResponseEntity udpateMember(@PathVariable("memberId") Long memberId,
                                                     @Valid @ModelAttribute MemberDto memberDto,
                                                     BindingResult bindingResult,
                                                     Principal principal){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<String>("다시 입력해주세요",HttpStatus.BAD_REQUEST);
        }else{
            memberDto.setEmail("email");
            memberService.updateMember(memberDto);
        }
        return new ResponseEntity<String>("수정이 완료되었습니다",HttpStatus.OK);
    }

    //회원 삭제
    @DeleteMapping("/member/{memberId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("memberId") Long memberId,Principal principal){
        try {
            memberService.deleteMember(memberId);
            return ResponseEntity.ok("탈퇴되었습니다.");
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다.");
        }
    }

}
