package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.Rank;
import com.tkf.teamkimfood.domain.Recipe;
import com.tkf.teamkimfood.domain.RecipeDetail;
import com.tkf.teamkimfood.domain.prefer.RecipeCategory;
import com.tkf.teamkimfood.dto.aboutrecipe.CategoryPreferenceDto;
import com.tkf.teamkimfood.dto.aboutrecipe.RecipeDetailDto;
import com.tkf.teamkimfood.dto.aboutrecipe.RecipeDetailListDto;
import com.tkf.teamkimfood.dto.aboutrecipe.RecipeDto;
import com.tkf.teamkimfood.dto.ranks.RankDto;
import com.tkf.teamkimfood.repository.MemberRepository;
import com.tkf.teamkimfood.repository.rank.RankQueryRepository;
import com.tkf.teamkimfood.repository.rank.RankRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeCategoryRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeDetailRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RankServiceTest {

    @Autowired
    RankRepository rankRepository;
    @Autowired
    RankQueryRepository rankQueryRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    RecipeCategoryRepository recipeCategoryRepository;
    @Autowired
    RecipeDetailRepository recipeDetailRepository;

    public Member createMember() {
        return Member.builder()
                .nickname("test")
                .build();
    }
    public CategoryPreferenceDto createCategoryPreferenceDto() {
        return CategoryPreferenceDto.builder()
                .foodNationType("Korean")
                .foodStuff("Meat")
                .situation("Alone")
                .build();
    }
    public List<RecipeDetailListDto> createRecipeDetailListDtos() {
        List<RecipeDetailListDto> recipeDetailListDtos = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            RecipeDetailListDto recipeDetailListDto = new RecipeDetailListDto();
            recipeDetailListDto.setDosage("테스트용량"+i);
            recipeDetailListDto.setIngredients("테스트재료"+i);
            recipeDetailListDtos.add(recipeDetailListDto);
        }
        return recipeDetailListDtos;
    }

    public RecipeDto createRecipeDto() {
        return RecipeDto.builder()
                .title("테스트제목")
                .content("테스트내용")
                .build();
    }
    public Rank createRank() {
        return Rank.builder()
                .build();
    }

    @Test
    public void 추천수증감테스트() {
        Member member = createMember();
        Member save = memberRepository.save(member);
        RecipeDto recipeDto = createRecipeDto();
        CategoryPreferenceDto categoryPreferenceDto = createCategoryPreferenceDto();
        List<RecipeDetailListDto> recipeDetailListDto = createRecipeDetailListDtos();
        Rank rank = createRank();
        //when
        Recipe recipe = Recipe.builder()
                .title(recipeDto.getTitle())
                .content(recipeDto.getContent())
                .writeDate(LocalDateTime.now())
                .correctionDate(LocalDateTime.now())
                .build();
        RecipeCategory recipeCategory = RecipeCategory.builder()
                .foodNationType(categoryPreferenceDto.getFoodNationType())
                .foodStuff(categoryPreferenceDto.getFoodStuff())
                .situation(categoryPreferenceDto.getSituation())
                .build();
        RecipeDetailDto recipeDetailDto = new RecipeDetailDto();
        List<RecipeDetail> recipeDetails = recipeDetailDto.ListDtoToListEntity(recipeDetailListDto);

        recipe.createRecipe(recipeDetails, save, recipeCategory);

        recipeRepository.save(recipe);
        recipeCategory.setRecipe(recipe);//연관관계 메소드 꼭 해줘야함. 아니면 NullPointException납니다.+JPA save시 레시피 아이디값이 안들어갑니다.
        recipeCategoryRepository.save(recipeCategory);//레시피와
        for (RecipeDetail recipeDetail : recipeDetails) {//꼭 해줘야함. 아니면 NullPointException납니다.
            recipeDetail.setRecipe(recipe);//레시피와 같이 들어가서 여기서 해결
        }
        recipeDetailRepository.saveAll(recipeDetails);

        rank.setMember(save);
        rank.setRecipe(recipe);
        Rank saved = rankRepository.save(rank);
        RankDto rankDto = new RankDto();
        rankDto.setId(saved.getId());
        rankDto.setRecipeRecommendation(saved.isRecipeRecommendation());
        rankDto.setUserRecommendation(saved.isUserRecommendation());

        //추천을 주기 위해서
        rankDto.setRecipeRecommendation(true);
        rank.recipeRecommend(rankDto);
        rankRepository.save(rank);
        Long total = rankQueryRepository.recommendationTotal(recipe.getId());

        assertNotNull(total);
        assertTrue(rankDto.isRecipeRecommendation());
    }

}