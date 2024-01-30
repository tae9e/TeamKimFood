package com.tkf.teamkimfood.controller;

import com.tkf.teamkimfood.dto.RecipeCategoryDto;
import com.tkf.teamkimfood.service.RecipeCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/survey")
public class RecipeCategoryController {
    private final RecipeCategoryService recipeCategoryService;

    @PostMapping("/list")
    public @ResponseBody ResponseEntity createSurvey(@RequestBody @Valid RecipeCategoryDto recipeCategoryDto,
                                                     BindingResult result){
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body("설문을 하나 이상 선택해주세요.");
        }
        recipeCategoryService.submitSurvey(recipeCategoryDto);
        return ResponseEntity.ok("설문조사 완료");
    }

    //설문조사 수정
    @PutMapping("/modify/{mpreferenceId}")
    public @ResponseBody ResponseEntity surveyUpdate(@PathVariable("mpreferenceId") Long mpreferenceId, @Valid @RequestBody  RecipeCategoryDto recipeCategoryDto,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("등록된 설문조사가 없습니다");
        }
        recipeCategoryDto.setId(mpreferenceId);
        recipeCategoryService.updateSurvey(recipeCategoryDto);
        return ResponseEntity.ok("수정되었습니다");
    }

}
