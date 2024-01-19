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
import com.tkf.teamkimfood.exception.NoAuthorityException;
import com.tkf.teamkimfood.repository.recipe.RecipeCategoryRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeDetailRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeRepository;
import com.tkf.teamkimfood.repository.query.MemberQueryRepository;
import com.tkf.teamkimfood.repository.query.RecipeQueryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    //게시글 1개 보려고 선택시 + 뷰카운트 올리기.
    public RecipeDto findOneById(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        recipe.addViewCount();//조회수 1 증가
        return RecipeDto.builder()
                .title(recipe.getTitle())
                .content(recipe.getContent())
                .writeDate(recipe.getWriteDate())
                .correctionDate(recipe.getCorrectionDate())
                .build();
    }

    //일반적인 메뉴노출(비회원 접속시)
    public List<RecipeDto> anyoneFindAll() {
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
    //게시글 수정. 사진도 파라미터로 추가해야함 챗 지피티를 활용해 좀 더 안전하게 만들어봤음
    @Transactional
    public Long updateRecipe(Long memberId ,Long recipeId, RecipeDto recipeDto) {
        Recipe recipe = recipeQueryRepository.findOneWhereMemberIdAndRecipeId(memberId, recipeId);
        Recipe updateRecipe = Recipe.builder()
                .title(recipeDto.getTitle())
                .content(recipeDto.getContent())
                .correctionDate(LocalDateTime.now())
                .build();
        recipe.updateWith(updateRecipe);
        recipeRepository.save(recipe);
        return recipe.getId();
    }
    //조회수 기준으로 조회요청시
    public List<RecipeDto> findAllOrderByViewCount() {
        return recipeQueryRepository.findAllOrderByViewCount().stream()
                .map(r->RecipeDto.builder()
                        .title(r.getTitle())
                        .content(r.getContent())
                        .writeDate(r.getWriteDate())
                        .correctionDate(r.getCorrectionDate())
                        .build())
                .toList();
    }
    //레시피 삭제
    public void deleteRecipe(Long memberId, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();
        if (memberId.equals(recipe.getMember().getId())) {
            recipeRepository.delete(recipe);
        } else {
            //권한 관련 예외문
            throw new NoAuthorityException("해당 레시피의 작성자가 아닙니다.");
        }
    }
}
