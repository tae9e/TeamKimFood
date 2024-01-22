package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.domain.FoodImg;
import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.Recipe;
import com.tkf.teamkimfood.domain.RecipeDetail;
import com.tkf.teamkimfood.domain.prefer.RecipeCategory;
import com.tkf.teamkimfood.dto.*;
import com.tkf.teamkimfood.dto.aboutrecipe.*;
import com.tkf.teamkimfood.exception.NoAuthorityException;
import com.tkf.teamkimfood.repository.query.MemberQueryRepository;
import com.tkf.teamkimfood.repository.query.RecipeQueryRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeCategoryRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeDetailRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {

    private final FoodImgService foodImgService;
    private final RecipeRepository recipeRepository;
    private final RecipeQueryRepository recipeQueryRepository;
    private final MemberQueryRepository memberRepository;
    private final RecipeDetailRepository recipeDetailRepository;
    private final RecipeCategoryRepository recipeCategoryRepository;


    //레시피 저장...
    @Transactional
    public Long saveRecipe(Long memberId, RecipeDto recipeDto, CategoryPreferenceDto categoryPreferenceDto, List<RecipeDetailListDto> recipeDetailListDto, List<MultipartFile> foodImgFileList) throws IOException {
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
        //이미지 저장
        for (int i = 0; i < foodImgFileList.size(); i++) {
            FoodImg foodImg = new FoodImg();
            foodImg.setRecipe(recipe);
            if (i == 0) {
                foodImg.setRepImgYn("Y");
            } else {
                foodImg.setRepImgYn("N");
            }
            foodImgService.saveFoodImg(foodImg, recipeDto.getFoodImgDtos().get(i).getExplains().get(i), foodImgFileList.get(i));
        }
        return recipe.getId();
    }

    //게시글 1개 보려고 선택시 + 뷰카운트 올리기.
//    public RecipeDto findOneById(Long id) {
//        Recipe recipe = recipeRepository.findById(id).orElseThrow(EntityExistsException::new);
//        recipe.addViewCount();//조회수 1 증가
//        //이미지 불러오기
//        List<FoodImg> foodImgs = foodImgRepository.findByRecipeIdOrderByIdAsc(id);
//        List<FoodImgDto> foodImgDtos = new ArrayList<>();
//        for (FoodImg foodImg : foodImgs) {
//            FoodImgDto foodImgDto = FoodImgDto.imgToDto(foodImg);
//            foodImgDtos.add(foodImgDto);
//        }
//        //레시피 용법용량 출력을위해 추가
//        List<RecipeDetail> recipeDetails = recipeDetailRepository.findAllWhereRecipeId(id);
//        RecipeDetailDto recipeDetailDto = new RecipeDetailDto();
//        recipeDetailDto.EntityToListDto(recipeDetails);
//        //레시피 카테고리 출력을 위해
//        RecipeCategory recipeCategory = recipeCategoryRepository.findWhereRecipeId(id);
//        CategoryPreferenceDto categoryPreferenceDto =
//                new CategoryPreferenceDto(recipeCategory.getSituation(), recipeCategory.getFoodStuff(), recipeCategory.getFoodNationType());
//        return RecipeDto.builder()
//                .title(recipe.getTitle())
//                .content(recipe.getContent())
//                .writeDate(recipe.getWriteDate())
//                .correctionDate(recipe.getCorrectionDate())
//                .recipeDetailDto(recipeDetailDto)
//                .categoryPreferenceDto(categoryPreferenceDto)
//                .foodImgDtos(foodImgDtos)
//                .build();
//    }
    public OneRecipeDto viewOne(Long recipeId) {
        OneRecipeDto recipeDto = recipeQueryRepository.getOne(recipeId);
        if (recipeDto != null) {
            Recipe recipe = recipeRepository.addViewCount(recipeId);
            recipeDto.setViewCount(recipe.getViewCount());
            return recipeDto;
        } else {
            return null;
        }
    }

    //    일반적인 메뉴노출(비회원 접속시)
    public Page<MainpageRecipeDto> getMain(RecipeSearchDto recipeSearchDto, Pageable pageable) {
        return recipeQueryRepository.getMainRecipePage(recipeSearchDto, pageable);
    }

    //내가 쓴 글 조회시 이거 써주세요 (회원이 마이페이지에서 내가 쓴 글 조회 클릭시 hidden으로 id값을 받는다.)
//    public List<RecipeDto> findAllWhereMemberId(Long id) {
//        return recipeQueryRepository.findAllWhereMemberId(id)
//                .stream()
//                .map(r -> RecipeDto.builder()
//                        .title(r.getTitle())
//                        .content(r.getContent())
//                        .writeDate(r.getWriteDate())
//                        .correctionDate(r.getCorrectionDate())
//                        .build())
//                .toList();
//    }
    public Page<MemberWriteRecipeDto> getMyRecipes(Long memberId, Pageable pageable) {
        return recipeQueryRepository.getRecipesWriteByMemberId(memberId, pageable);
    }
    //추천 레시피 띄우기 멤버가 설성한 선호타입을 정해진 값으로 받고 찾는다.
//    public List<RecipeDto> findAllWhereRecipeCategoryOrderByWriteDateDesc(MemberPreference memberPreference) {
//        RecipeCategory recipeCategory = RecipeCategory.builder()
//                .situation(memberPreference.getSituation())
//                .foodStuff(memberPreference.getFoodStuff())
//                .foodNationType(memberPreference.getFoodNationType())
//                .build();
//       return recipeQueryRepository.findAllWhereRecipeCategoryOrderByWriteDateDesc(recipeCategory)
//                .stream()
//                .map(r -> RecipeDto.builder()
//                        .title(r.getTitle())
//                        .content(r.getContent())
//                        .writeDate(r.getWriteDate())
//                        .correctionDate(r.getCorrectionDate())
//                        .build())
//                .toList();
//    }
    public Page<MainpageRecipeDto> getMainForMember(CategoryPreferenceDto categoryPreferenceDto, RecipeSearchDto recipeSearchDto, Pageable pageable) {
        Member member = memberRepository.findOne(categoryPreferenceDto.getId());
        categoryPreferenceDto.setSituation(member.getMemberPreference().getSituation());
        categoryPreferenceDto.setFoodStuff(member.getMemberPreference().getFoodStuff());
        categoryPreferenceDto.setFoodNationType(member.getMemberPreference().getFoodNationType());
        return recipeQueryRepository.getAllWhereTypesOrderByWriteDay(categoryPreferenceDto, recipeSearchDto, pageable);
    }
    //게시글 수정. 사진도 파라미터로 추가해야함 챗 지피티를 활용해 좀 더 안전하게 만들어봤음
    @Transactional
    public Long updateRecipe(Long memberId , Long recipeId, FoodImgDto foodImgDto, List<MultipartFile> foodImgFileList, RecipeDto recipeDto) throws IOException {
        Recipe recipe = recipeQueryRepository.findOneWhereMemberIdAndRecipeId(memberId, recipeId);
        Recipe updateRecipe = Recipe.builder()
                .title(recipeDto.getTitle())
                .content(recipeDto.getContent())
                .correctionDate(LocalDateTime.now())
                .build();
        //레시피 수정 적용
        recipe.updateWith(updateRecipe);
        if (foodImgDto != null) {
            List<Long> foodImgIds = foodImgDto.getFoodImgIds();
            //이미지수정
            for (int i = 0; i < foodImgFileList.size(); i++) {
                foodImgService.updateFoodImg(foodImgIds.get(i), foodImgDto.getExplains().get(i) ,foodImgFileList.get(i));
            }
        }
        recipeRepository.save(recipe);
        return recipe.getId();
    }

    //조회수 기준으로 조회요청시
//    public List<RecipeDto> findAllOrderByViewCount() {
//        return recipeQueryRepository.findAllOrderByViewCount().stream()
//                .map(r->RecipeDto.builder()
//                        .title(r.getTitle())
//                        .content(r.getContent())
//                        .writeDate(r.getWriteDate())
//                        .correctionDate(r.getCorrectionDate())
//                        .build())
//                .toList();
//    }
    public Page<MainpageRecipeDto> findAllOrderByViewCount(Pageable pageable) {
        return recipeQueryRepository.getAllOrderByViewCount(pageable);
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
    //추천수기반 조회
    public Page<MainpageRecipeDto> getAllOrderByRankPoint(Pageable pageable) {
        return recipeQueryRepository.getAllOrderByRankPoint(pageable);
    }
}
