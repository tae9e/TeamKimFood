package com.tkf.teamkimfood.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor

public class LoginController {

    @GetMapping("/member")
    public String Home(){
    return "login/MemberLogin";
    }
}
