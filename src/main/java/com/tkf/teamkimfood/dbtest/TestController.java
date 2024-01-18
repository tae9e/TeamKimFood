package com.tkf.teamkimfood.dbtest;

import com.tkf.teamkimfood.config.jwt.AuthTokens;
import com.tkf.teamkimfood.infra.KakaoLoginParams;
import com.tkf.teamkimfood.service.OAuthLoginService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class TestController {

    private final TestService testService;


    @PostMapping("/test/join")
    public Long join(JoinEx joinEx){
        return testService.join(joinEx);
    }
    @GetMapping("/test/list")
    public List<TestDto> view(){
        return testService.findAll();
    }
    @GetMapping("/api/data")
    public String test(){
        return "Hello, React";
    }



}

