package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.Recipe;
import com.tkf.teamkimfood.domain.RecipeDetail;
import com.tkf.teamkimfood.domain.prefer.RecipeCategory;
import com.tkf.teamkimfood.dto.CategoryPreferenceDto;
import com.tkf.teamkimfood.dto.RecipeDetailDto;
import com.tkf.teamkimfood.dto.RecipeDetailListDto;
import com.tkf.teamkimfood.dto.RecipeDto;
import com.tkf.teamkimfood.repository.recipe.RecipeCategoryRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeDetailRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeRepository;
import com.tkf.teamkimfood.repository.query.MemberQueryRepository;
import com.tkf.teamkimfood.repository.query.RecipeDetailQueryRepository;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
class RecipeServiceTest {

    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    MemberQueryRepository memberRepository;
    @Autowired
    RecipeDetailRepository recipeDetailRepository;
    @Autowired
    RecipeCategoryRepository recipeCategoryRepository;
    @Autowired
    RecipeDetailQueryRepository recipeDetailQueryRepository;

    //아직 다른 엔티티들은 미구현이라 없음
    public Member createMember() {
        return Member.builder()
                .id(1L)//테스트 하시기전에 생성자에 id도 넣어주세요
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

    @Test
    void 레시피추가( ) throws Exception {
        //given
        Member member = createMember();
        RecipeDto recipeDto = createRecipeDto();
        CategoryPreferenceDto categoryPreferenceDto = createCategoryPreferenceDto();
        List<RecipeDetailListDto> recipeDetailListDto = createRecipeDetailListDtos();
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

        recipe.createRecipe(recipeDetails, member, recipeCategory);

        recipeRepository.save(recipe);
        recipeCategory.setRecipe(recipe);//연관관계 메소드 꼭 해줘야함. 아니면 NullPointException납니다.+JPA save시 레시피 아이디값이 안들어갑니다.
        recipeCategoryRepository.save(recipeCategory);//레시피와
        for (RecipeDetail recipeDetail : recipeDetails) {//꼭 해줘야함. 아니면 NullPointException납니다.
            recipeDetail.setRecipe(recipe);//레시피와 같이 들어가서 여기서 해결
        }
        recipeDetailRepository.saveAll(recipeDetails);

        Long id1 = recipe.getId();
        Long id2 = recipeCategory.getRecipe().getId();
        Long id3 = recipeDetails.get(1).getRecipe().getId();

        //then
        assertNotNull(id1, "값이 있습니다.");
        assertEquals(id1,id2,id3);//값이 같다.
    }
}