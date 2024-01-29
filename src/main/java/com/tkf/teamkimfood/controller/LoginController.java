package com.tkf.teamkimfood.controller;


import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.dto.MemberFormDto;
import com.tkf.teamkimfood.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor

public class LoginController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

//    @GetMapping("/member")
//    public String loginMember(Model model){
//    model.addAttribute("memberFormDto",new MemberFormDto());
//    return "login/memberLogin";
//    }
//
//    @PostMapping("/member")
//    public String createMember(@Valid MemberFormDto memberFormDto , BindingResult bindingResult, Model model){
//
//        if(bindingResult.hasErrors()){
//            return "login/memberLogin";
//        }
//
//        try{
//            Member member = Member.createMember(memberFormDto,passwordEncoder);
//            memberService.saveMember(member);
//            return "login/success";
//        }catch(IllegalStateException e){
//            model.addAttribute("errorMessage",e.getMessage());
//            return "login/memberLogin";
//        }
//
//    }
    @PostMapping("/member")
    public @ResponseBody ResponseEntity createMember(@Valid MemberFormDto memberFormDto,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("회원가입에 실패했습니다. 유효성 메시지를 확인해주세요");
        }

        try{
            Member member = Member.createMember(memberFormDto,passwordEncoder);
            memberService.saveMember(member);
            return ResponseEntity.ok().body("회원가입 성공");
        }catch(IllegalStateException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //회원 수정
    @PutMapping("/member/update/{memberId}")
    public @ResponseBody ResponseEntity udpateMember(@PathVariable("memberId") Long memberId,
                                                     @Valid @RequestBody MemberFormDto memberFormDto,
                                                     BindingResult bindingResult,
                                                     Principal principal){
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("수정 실패");
        }else{
            memberFormDto.setMemberId(memberId);
            memberService.updateMember(memberFormDto);
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
