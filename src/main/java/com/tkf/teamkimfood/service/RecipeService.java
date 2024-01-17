package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.Recipe;
import com.tkf.teamkimfood.domain.RecipeDetail;
import com.tkf.teamkimfood.domain.prefer.MemberPreference;
import com.tkf.teamkimfood.domain.prefer.RecipeCategory;
import com.tkf.teamkimfood.dto.CategoryPreferenceDto;
import com.tkf.teamkimfood.dto.RecipeDetailDto;
import com.tkf.teamkimfood.dto.RecipeDetailListDto;
import com.tkf.teamkimfood.dto.RecipeDto;
import com.tkf.teamkimfood.repository.RecipeCategoryRepository;
import com.tkf.teamkimfood.repository.RecipeDetailRepository;
import com.tkf.teamkimfood.repository.RecipeRepository;
import com.tkf.teamkimfood.repository.query.MemberQueryRepository;
import com.tkf.teamkimfood.repository.query.RecipeQueryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {

    RecipeRepository recipeRepository;
    RecipeQueryRepository recipeQueryRepository;
    MemberQueryRepository memberRepository;
    RecipeDetailRepository recipeDetailRepository;
    RecipeCategoryRepository recipeCategoryRepository;

    //레시피 저장...
    @Transactional
    public Long saveRecipe(Long memberId, RecipeDto recipeDto, CategoryPreferenceDto categoryPreferenceDto, List<RecipeDetailListDto> recipeDetailListDto) {
        Member member = memberRepository.findOne(memberId);
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
        recipeCategory.setRecipe(recipe);//연관관계 메소드 영속성 유지를 위해 꼭 해줘야함. 아니면 NullPointException납니다.+JPA save시 레시피 아이디값이 안들어갑니다.
        recipeCategoryRepository.save(recipeCategory);//레시피와 같이 들어가서 여기서 해결
        for (RecipeDetail recipeDetail : recipeDetails) {//영속성 유지를 위해 꼭 해줘야함. 아니면 NullPointException납니다.
            recipeDetail.setRecipe(recipe);//레시피와 같이 들어가서 여기서 해결
        }
        recipeDetailRepository.saveAll(recipeDetails);
        return recipe.getId();
    }
    //게시글 1개 보려고 선택시
    public Recipe findOneById(Long id) {
        return recipeRepository.findById(id).orElseThrow();
    }

    //일반적인 메뉴노출(전체, 잘 안쓸것같긴합니다.)
    public List<RecipeDto> findAll() {
        return recipeRepository.findAllByOrderByWriteDateDesc().stream()
                .map(r-> RecipeDto.builder()
                        .title(r.getTitle())
                        .content(r.getContent())
                        .writeDate(r.getWriteDate())
                        .correctionDate(r.getCorrectionDate())
                        .build())
                .toList();
    }
    //내가 쓴 글 조회시 이거 써주세요 (회원이 마이페이지에서 내가 쓴 글 조회 클릭시 hidden으로 id값을 받는다.)
    public List<RecipeDto> findAllWhereMemberId(Long id) {
        return recipeQueryRepository.findAllWhereMemberId(id)
                .stream()
                .map(r -> RecipeDto.builder()
                        .title(r.getTitle())
                        .content(r.getContent())
                        .writeDate(r.getWriteDate())
                        .correctionDate(r.getCorrectionDate())
                        .build())
                .toList();
    }
    //추천 레시피 띄우기 멤버가 설성한 선호타입을 정해진 값으로 받고 찾는다.
    public List<RecipeDto> findAllWhereRecipeCategoryOrderByWriteDateDesc(MemberPreference memberPreference) {
        RecipeCategory recipeCategory = RecipeCategory.builder()
                .situation(memberPreference.getSituation())
                .foodStuff(memberPreference.getFoodStuff())
                .foodNationType(memberPreference.getFoodNationType())
                .build();
       return recipeQueryRepository.findAllWhereRecipeCategoryOrderByWriteDateDesc(recipeCategory)
                .stream()
                .map(r -> RecipeDto.builder()
                        .title(r.getTitle())
                        .content(r.getContent())
                        .writeDate(r.getWriteDate())
                        .correctionDate(r.getCorrectionDate())
                        .build())
                .toList();
    }
    //사진도 파라미터로 추가해야함 챗 지피티를 활용해 좀 더 안전하게 만들어봤음
    @Transactional
    public Long updateRecipe(Long id, RecipeDto recipeDto) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(()->new NoSuchElementException("찾으시는 레시피가 없습니다."+id));
        Recipe updateRecipe = Recipe.builder()
                .title(recipeDto.getTitle())
                .content(recipeDto.getContent())
                .correctionDate(LocalDateTime.now())
                .build();
        recipe.updateWith(updateRecipe);
        recipeRepository.save(recipe);
        return recipe.getId();
    }
}
