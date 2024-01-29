package com.tkf.teamkimfood.controller;

import com.tkf.teamkimfood.dto.RecipeCategoryDto;
import com.tkf.teamkimfood.service.RecipeCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class RecipeCategoryController {
    private final RecipeCategoryService recipeCategoryService;

    @PostMapping("/list")
    public @ResponseBody ResponseEntity createSurvey(@RequestBody @Valid RecipeCategoryDto recipeCategoryDto,
                                                     BindingResult result){
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body("설문조사는 하나 이상 선택해주세요");
        }
        recipeCategoryService.submitSurvey(recipeCategoryDto);
        return ResponseEntity.ok("설문조사 완료");
    }
}
