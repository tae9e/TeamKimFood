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
    @Transactional
    public RecipeCategoryDto getSurveyResults(Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            RecipeCategory recipeCategory = recipeCategoryRepository.findByMember(member);
            RecipeCategoryDto recipeCategoryDto = new RecipeCategoryDto();
//            RecipeCategoryDto recipeCategoryDto = new RecipeCategoryDto();
                if(recipeCategory == null){
                    return null;
                }else {

                    recipeCategoryDto.setSituation(recipeCategory.getSituation());
                    recipeCategoryDto.setFoodStuff(recipeCategory.getFoodStuff());
                    recipeCategoryDto.setFoodNationType(recipeCategory.getFoodNationType());
                }

            return recipeCategoryDto;
        } else {
            throw new IllegalArgumentException("회원 정보를 찾을 수 없습니다.");
        }
    }
    //설문조사 수정
    @Transactional
    public void updateSurvey(RecipeCategoryDto recipeCategoryDto){
        RecipeCategory recipeCategory=recipeCategoryRepository.findById(recipeCategoryDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        recipeCategory.setSituation(recipeCategoryDto.getSituation());
        recipeCategory.setFoodStuff(recipeCategory.getFoodStuff());
        recipeCategory.setFoodNationType(recipeCategoryDto.getFoodNationType());
        recipeCategoryRepository.save(recipeCategory);
    }


}
