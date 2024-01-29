package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.domain.prefer.RecipeCategory;
import com.tkf.teamkimfood.dto.RecipeCategoryDto;
import com.tkf.teamkimfood.repository.MemberRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeCategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class RecipeCategoryService {
    private final RecipeCategoryRepository recipeCategoryRepository;
    private final MemberRepository memberRepository;

    //설문조사 등록
    public RecipeCategory submitSurvey(RecipeCategoryDto recipeCategoryDto) {

        RecipeCategory survey = RecipeCategory.builder()
                .foodStuff(recipeCategoryDto.getFoodStuff())
                .situation(recipeCategoryDto.getSituation())
                .foodNationType(recipeCategoryDto.getFoodNationType())
                .build();

        return recipeCategoryRepository.save(survey);

    }

    //설문조사 목록 조회


    //한 명의 설문조사 등록정보 조회


    //설문조사 삭제


}
