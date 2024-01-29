package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.prefer.RecipeCategory;
import com.tkf.teamkimfood.dto.RecipeCategoryDto;
import com.tkf.teamkimfood.repository.MemberRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeCategoryRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class RecipeCategoryService {
    private final RecipeCategoryRepository recipeCategoryRepository;
    private final MemberRepository memberRepository;


    //설문조사 등록
    public RecipeCategory submitSurvey(RecipeCategoryDto recipeCategoryDto) {

        RecipeCategory survey = RecipeCategory.builder()
                .situation(recipeCategoryDto.getSituation())
                .foodStuff(recipeCategoryDto.getFoodStuff())
                .foodNationType(recipeCategoryDto.getFoodNationType())
                .build();

        return recipeCategoryRepository.save(survey);

    }

    //설문조사 LIST 불러오기
    @Transactional(readOnly = true)
    public RecipeCategoryDto getSurveyResult(String email) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            List<RecipeCategory> recipeCategoryList = recipeCategoryRepository.findByMember(member);
            if (!recipeCategoryList.isEmpty()) {
                //RecipeCategory recipeCategory = recipeCategoryList.get(recipeCategoryList);
//                RecipeCategoryDto categoryDto = new RecipeCategoryDto();
//                categoryDto.setFoodStuff(recipeCategory.getFoodStuff());
//                categoryDto.setSituation(recipeCategory.getSituation());
//                categoryDto.setFoodNationType(recipeCategory.getFoodNationType());
//                return categoryDto;
            }
        }
        return null;
    }

    //설문조사 수정
    @Transactional
    public void updateSurvey(RecipeCategoryDto recipeCategoryDto){
        RecipeCategory recipeCategory=recipeCategoryRepository.findById(recipeCategoryDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        recipeCategory.getSituation();
        recipeCategory.getFoodStuff();
        recipeCategory.getFoodNationType();
        recipeCategoryRepository.save(recipeCategory);
    }


}
