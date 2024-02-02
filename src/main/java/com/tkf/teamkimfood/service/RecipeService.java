package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.domain.FoodImg;
import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.Recipe;
import com.tkf.teamkimfood.domain.RecipeDetail;
import com.tkf.teamkimfood.domain.prefer.RecipeCategory;
import com.tkf.teamkimfood.dto.*;
import com.tkf.teamkimfood.dto.aboutrecipe.*;
import com.tkf.teamkimfood.exception.NoAuthorityException;
import com.tkf.teamkimfood.repository.FoodImgRepository;
import com.tkf.teamkimfood.repository.MemberRepository;
import com.tkf.teamkimfood.repository.query.MemberQueryRepository;
import com.tkf.teamkimfood.repository.query.RecipeQueryRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeCategoryRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeDetailRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RecipeService {

    private final FoodImgService foodImgService;
    private final RecipeRepository recipeRepository;
    private final RecipeQueryRepository recipeQueryRepository;
    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final RecipeDetailRepository recipeDetailRepository;
    private final RecipeCategoryRepository recipeCategoryRepository;
    private final FoodImgRepository foodImgRepository;


    //레시피 저장...
    @Transactional
    public Long saveRecipe(Long userId, RecipeDto recipeDto, CategoryPreferenceDto categoryPreferenceDto, List<RecipeDetailListDto> recipeDetailListDto,List<String> explanations, List<MultipartFile> foodImgFileList, int repImageIndex) throws IOException {
        log.info("이메일 : "+userId);
        Member member = memberRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Member with email " + userId + " not found"));
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
//        List<RecipeDetail> recipeDetails = recipeDetailDto.ListDtoToListEntity(recipeDetailListDto);
        List<RecipeDetail> recipeDetails = new ArrayList<>();
        for (RecipeDetailListDto detailListDto : recipeDetailListDto) {
            RecipeDetail build = RecipeDetail.builder()
                    .ingredients(detailListDto.getIngredients())
                    .dosage(detailListDto.getDosage())
                    .build();
            recipeDetails.add(build);
        }

        Recipe savedRecipe = recipe.createRecipe(recipeDetails, member, recipeCategory);
        savedRecipe = Recipe.builder()
                        .title(recipe.getTitle())
                                .content(recipe.getContent())
                                        .writeDate(recipe.getWriteDate())
                                                .correctionDate(recipe.getCorrectionDate())
                                                        .build();
        savedRecipe.setMember(member);


        recipeRepository.save(savedRecipe);
        recipeCategory.setRecipe(savedRecipe);//연관관계 메소드 영속성 유지를 위해 꼭 해줘야함. 아니면 NullPointException납니다.+JPA save시 레시피 아이디값이 안들어갑니다.
        recipeCategoryRepository.save(recipeCategory);//레시피와 같이 들어가서 여기서 해결
        for (RecipeDetail recipeDetail : recipeDetails) {//영속성 유지를 위해 꼭 해줘야함. 아니면 NullPointException납니다.
            recipeDetail.setRecipe(savedRecipe);//레시피와 같이 들어가서 여기서 해결
        }
        recipeDetailRepository.saveAll(recipeDetails);
        //이미지 저장
        for (int i = 0; i < foodImgFileList.size(); i++) {
            FoodImg foodImg = new FoodImg();
            foodImg.setRecipe(savedRecipe);
            if (i == repImageIndex) {
                foodImg.setRepImgYn("Y");
            } else {
                foodImg.setRepImgYn("N");
            }
            foodImgService.saveFoodImg(foodImg, explanations.get(i), foodImgFileList.get(i));
        }
        return savedRecipe.getId();
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
    @Transactional
    public OneRecipeDto viewOne(Long recipeId) {
        OneRecipeDto recipeDto = recipeQueryRepository.getOne(recipeId);

        // 조회 수를 업데이트
        recipeRepository.addViewCount(recipeId);

        // 업데이트된 Recipe 객체를 다시 가져옴
        Recipe updatedRecipe = recipeRepository.findById(recipeId).orElse(null);

        if (updatedRecipe != null) {
            recipeDto.setViewCount(updatedRecipe.getViewCount());
        } else {
            // Recipe가 존재하지 않는 경우에 대한 처리
            // 예를 들어, 적절한 에러 메시지를 설정하거나 예외를 던질 수 있습니다.
        }

        return recipeDto;
    }
    public List<OneRecipeImgVo> viewOneForOne(Long recipeId) {
        return recipeQueryRepository.getImgExp(recipeId);
    }
    public List<OneRecipeIngDoVo> getOneForOne(Long recipeId) {
        return recipeQueryRepository.getIngDo(recipeId);
    }


    public OneRecipeForUpdateVo findOneByEmail(Long recipeId, Long userId) {
        return recipeQueryRepository.findOneByEmail(recipeId, userId);
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
//        Member member = memberQueryRepository.findOne(categoryPreferenceDto.getId());
//
//        categoryPreferenceDto.setSituation(member.getMemberPreference().getSituation());
//        categoryPreferenceDto.setFoodStuff(member.getMemberPreference().getFoodStuff());
//        categoryPreferenceDto.setFoodNationType(member.getMemberPreference().getFoodNationType());
        return recipeQueryRepository.getAllWhereTypesOrderByWriteDay(categoryPreferenceDto, recipeSearchDto, pageable);
    }
    //게시글 수정. 사진도 파라미터로 추가해야함 챗 지피티를 활용해 좀 더 안전하게 만들어봤음
    @Transactional
    public Long updateRecipe(Long memberId,Long recipeId, List<String> explanations, List<MultipartFile> foodImgFileList, RecipeDto recipeDto, int repImageIndex,List<String> existingImgUrlList, List<String> existingExplanations) throws IOException {
        Recipe checking = recipeRepository.findById(recipeId).orElseThrow();
        if (checking.getMember().getId().equals(memberId)) {
            Recipe recipe = recipeQueryRepository.findOneWhereMemberIdAndRecipeId(memberId, recipeId);//findOne처럼 수정해야함
            Recipe updateRecipe = Recipe.builder()
                    .title(recipeDto.getTitle())
                    .content(recipeDto.getContent())
                    .correctionDate(LocalDateTime.now())
                    .build();
            //레시피 수정 적용
            recipe.updateWith(updateRecipe);
//            foodImgDto.setRepImgYn(String.valueOf(repImageIndex));

            List<FoodImgDto> foodImgDto = new ArrayList<>();
            List<FoodImg> foodImgs = foodImgRepository.findByRecipeIdOrderByIdAsc(recipeId);
            for (FoodImg foodImg : foodImgs) {
                FoodImgDto imgDto = new FoodImgDto();
                imgDto.setRepImgYn(foodImg.getRepImgYn());
                imgDto.setImgName(foodImg.getImgName());
                imgDto.setImgUrl(foodImg.getImgUrl());
                imgDto.setOriginImgName(foodImg.getOriginImgName());
                imgDto.setFoodId(foodImg.getId());
                foodImgDto.add(imgDto);
            }

            if (foodImgDto != null) {
                List<Long> foodImgIds = foodImgDto.stream().map(fid->fid.getFoodId()).toList();
                log.info("아이디값 확인"+foodImgIds);
                //이미지수정
                for (int i = 0; i < foodImgIds.size(); i++) {
                    foodImgService.updateFoodImg(foodImgIds.get(i), explanations.get(i), foodImgFileList.get(i));
                }
            }
            recipeRepository.save(recipe);
            return recipe.getId();
        }else {
            return null;
        }
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
    @Transactional
    public Boolean deleteRecipe(Long memberId, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();
        if (memberId.equals(recipe.getMember().getId())) {
            recipeRepository.delete(recipe);
            return true;
        } else {
            //권한 관련 예외문
            throw new NoAuthorityException("해당 레시피의 작성자가 아닙니다.");
        }
    }
    //추천수기반 조회
    public Page<MainpageRecipeDto> getAllOrderByRankPoint(Pageable pageable) {
        return recipeQueryRepository.getAllOrderByRankPoint(pageable);
    }

    //레시피 자세히 보기
}
