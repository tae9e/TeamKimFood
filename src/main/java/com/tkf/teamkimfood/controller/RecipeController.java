package com.tkf.teamkimfood.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkf.teamkimfood.dto.CommentDto;
import com.tkf.teamkimfood.dto.RecipeCategoryDto;
import com.tkf.teamkimfood.dto.RecipeNCommentVo;
import com.tkf.teamkimfood.dto.aboutrecipe.*;
import com.tkf.teamkimfood.dto.MainpageRecipeDto;
import com.tkf.teamkimfood.dto.ranks.RankDto;
import com.tkf.teamkimfood.service.MemberService;
import com.tkf.teamkimfood.service.RankService;
import com.tkf.teamkimfood.service.RecipeCategoryService;
import com.tkf.teamkimfood.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor()
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;
    private final RankService rankService;
    private final MemberService memberService;
    private final RecipeCategoryService recipeCategoryService;
    //레시피 저장
//    @PostMapping("/api/recipes/save")
//    public ResponseEntity<Long> saveRecipe(@RequestBody RecipeRequestVo request) {
//        try {
//            Long saveRecipe = recipeService.saveRecipe(
//                    request.getMemberId(),
//                    request.getRecipeDto(),
//                    request.getCategoryPreferenceDto(),
//                    request.getRecipeDetailListDto(),
//                    request.getExplanations(),
//                    request.getFoodImgFileList()
//            );
//            return ResponseEntity.ok(saveRecipe);
//        } catch (IOException e) {
//            // 예외 처리
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(-1L);
//        }
//    }
    @PostMapping(value = "/api/recipe/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> saveRecipe(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("recipeRequest") String recipeRequest,
                                           @RequestParam("foodImgFileList") MultipartFile[] foodImgFileList,
                                           @RequestParam("repImageIndex") int repImageIndex) {
        try {
            // JSON 문자열을 RecipeRequestVo 객체로 변환
            RecipeRequestVo request = new ObjectMapper().readValue(recipeRequest, RecipeRequestVo.class);

            // 파일 리스트를 RecipeRequestVo 객체에 설정
            request.setFoodImgFileList(Arrays.asList(foodImgFileList));
            Long id = Long.valueOf(userDetails.getUsername());

            // 서비스 호출
            Long saveRecipe = recipeService.saveRecipe(
                    id,
                    request.getRecipeDto(),
                    request.getCategoryPreferenceDto(),
                    request.getRecipeDetailListDto(),
                    request.getExplanations(),
                    request.getFoodImgFileList(),
                    repImageIndex
            );

            return ResponseEntity.ok(saveRecipe);
        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(-1L);
        }
    }
    //메인화면 구성
    @GetMapping("/api/recipes/boardList")
    public Page<MainpageRecipeDto> getMain(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String search, @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        RecipeSearchDto recipeSearchDto = new RecipeSearchDto();

        if (userDetails != null && search == null) {//로그인한상태 and 레시피 서치 안한경우

            log.info("ㅇㅇ"+userDetails.getUsername());
            Long id = Long.valueOf(userDetails.getUsername());
            RecipeCategoryDto surveyResults = recipeCategoryService.getSurveyResults(id);
            if (surveyResults != null) {
                // memberId가 존재하는 경우
                //멤버가 따로 관심사 설정 안했을경우도 만들기.
                CategoryPreferenceDto categoryPreferenceDto = new CategoryPreferenceDto();
                categoryPreferenceDto.setId(id);
                categoryPreferenceDto.setSituation(surveyResults.getSituation());
                categoryPreferenceDto.setFoodStuff(surveyResults.getFoodStuff());
                categoryPreferenceDto.setFoodNationType(surveyResults.getFoodNationType());
                return recipeService.getMainForMember(categoryPreferenceDto, recipeSearchDto, pageable);
            } else {
                //선호도 조사 안했을경우
                return recipeService.getMain(recipeSearchDto, pageable);
            } 
            
        }else {
            // memberId가 없는 경우 || 검색 했을 경우
            recipeSearchDto.setSearchByLike(search);
            return recipeService.getMain(recipeSearchDto, pageable);
        }
        
    }
    //세부조회(댓글 필요)
    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<RecipeNCommentVo> viewOne(@PathVariable("id")Long recipeId, @AuthenticationPrincipal UserDetails userDetails){
        OneRecipeDto oneRecipeDto = recipeService.viewOne(recipeId);
        boolean equalMember = false;
        if (userDetails != null) {
            long id = Long.parseLong(userDetails.getUsername());

            if (id == oneRecipeDto.getMemberId()){
                equalMember = true;
            }
        }

        List<OneRecipeImgVo> oneRecipeImgVos = recipeService.viewOneForOne(recipeId);
        List<OneRecipeIngDoVo> oneRecipeIngDoVos = recipeService.getOneForOne(recipeId);
        Long rank = rankService.totalRecipeRank(recipeId);

        //현재 댓글은 빈 객체 돌려줌
        CommentDto commentDto = new CommentDto();//코멘트 service 구현 완료시 수정예정.
        RecipeNCommentVo recipeNCommentVo = new RecipeNCommentVo(oneRecipeDto, commentDto, oneRecipeImgVos, oneRecipeIngDoVos);
        recipeNCommentVo.setEqualMember(equalMember);
        recipeNCommentVo.setTotalScore(rank);
        return ResponseEntity.ok(recipeNCommentVo);
    }
    //수정
    @PutMapping("/api/recipes/{recipeId}")
    public ResponseEntity<Long> updateRecipe(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable("recipeId")Long recipeId,
                                             @RequestParam("recipeRequest") String recipeRequest,
                                             @RequestParam("foodImgFileList") MultipartFile[] foodImgFileList,
                                             @RequestParam("repImageIndex") int repImageIndex,
                                             @RequestParam(value = "existingImgUrlList", required = false) List<String> existingImgUrlList,
                                             @RequestParam(value = "existingExplanations", required = false) List<String> existingExplanations) {
        try {
            // JSON 문자열을 RecipeRequestVo 객체로 변환
            RecipeRequestVo request = new ObjectMapper().readValue(recipeRequest, RecipeRequestVo.class);

            // 파일 리스트를 RecipeRequestVo 객체에 설정
            request.setFoodImgFileList(Arrays.asList(foodImgFileList));
            Long id = Long.valueOf(userDetails.getUsername());
//String email , Long recipeId, FoodImgDto foodImgDto, List<String> explanations, List<MultipartFile> foodImgFileList, RecipeDto recipeDto
            // 서비스 호출
            Long saveRecipe = recipeService.updateRecipe(
                    id,
                    recipeId,
                    request.getExplanations(),
                    request.getFoodImgFileList(),
                    request.getRecipeDto(),
                    repImageIndex,
                    existingImgUrlList,
                    existingExplanations
            );

            return ResponseEntity.ok(saveRecipe);
        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(-1L);
        }
    }
    //로그인한 회원의 Long id 가져오기
    private Long getUserId(UserDetails userDetails) {
        if (userDetails instanceof LoginUserForRecipeVo) {
            return ((LoginUserForRecipeVo) userDetails).getUserId();
        } else {
            throw new RuntimeException("로그인이 확인되지 않습니다.");
        }
    }
    //레시피 1개 보기

    //수정할 레시피 내용 가져오기
    @GetMapping("/api/recipes/{id}")
    public ResponseEntity<?> getOneRecipeForUpdate(@PathVariable("id")Long recipeId, @AuthenticationPrincipal UserDetails userDetails) {
        OneRecipeDto oneRecipeDto = recipeService.viewOne(recipeId);
        Long id = Long.valueOf(userDetails.getUsername());
        OneRecipeForUpdateVo recipe = recipeService.findOneByEmail(recipeId, id);
        List<OneRecipeImgVo> oneRecipeImgVos = recipeService.viewOneForOne(recipeId);
        List<OneRecipeIngDoVo> oneForOne = recipeService.getOneForOne(recipeId);
        CommentDto commentDto = new CommentDto();//필요없는애
        RecipeNCommentVo recipeNCommentVo = new RecipeNCommentVo(oneRecipeDto, commentDto, oneRecipeImgVos, oneForOne);
        if (recipe != null) {
            return ResponseEntity.ok(recipeNCommentVo);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("레시피 정보를 가져오지 못했습니다. 본인이 작성한 레시피가 맞는지 확인해주세요");
        }
    }

    //삭제
    @DeleteMapping("/api/recipes/{id}/delete")
    public ResponseEntity<String> deleteRecipe(@PathVariable("id")Long recipeId, @AuthenticationPrincipal UserDetails userDetails) {
        Long id = Long.valueOf(userDetails.getUsername());
        Boolean success = recipeService.deleteRecipe(id, recipeId);

        if (success) {
            return ResponseEntity.ok("성공적으로 삭제했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제에 실패했습니다. 본인이 작성한 레시피가 맞는지 확인 부탁드립니다.");
        }
    }
    //레시피 추천
    @PostMapping("/api/recipes/{id}/recommend")
    public ResponseEntity<?> recommendRecipe(@PathVariable("id")Long recipeId,
                                             @AuthenticationPrincipal UserDetails userDetails) {
            if (userDetails != null) {
                Long memberId = Long.valueOf(userDetails.getUsername());
                RankDto rankDto = new RankDto(memberId, recipeId);
                Long totalRecommendation = rankService.recommRecipeVariation(rankDto);
                return ResponseEntity.ok(totalRecommendation);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("로그인을 하셔야 추천 할 수 있습니다.");
            }
    }

}
