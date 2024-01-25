package com.tkf.teamkimfood.controller;


import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.dto.MemberFormDto;
import com.tkf.teamkimfood.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
            return "/login/memberLogin";
        }

        try{
            Member member = Member.createMember(memberFormDto,passwordEncoder);
            memberService.saveMember(member);
        }catch(IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "/login/memberLogin";
        }

        return "login/hello";
    }

    @GetMapping("/hello")
    public String test(){
        return "login/hello";
    }
}
