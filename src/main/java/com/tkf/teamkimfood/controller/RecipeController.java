package com.tkf.teamkimfood.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkf.teamkimfood.dto.CommentDto;
import com.tkf.teamkimfood.dto.RecipeNCommentVo;
import com.tkf.teamkimfood.dto.aboutrecipe.*;
import com.tkf.teamkimfood.dto.MainpageRecipeDto;
import com.tkf.teamkimfood.service.RecipeService;
import lombok.RequiredArgsConstructor;
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
public class RecipeController {

    private final RecipeService recipeService;
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
    @PostMapping(value = "/api/recipes/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> saveRecipe(@RequestParam("recipeRequest") String recipeRequest,
                                           @RequestParam("foodImgFileList") MultipartFile[] foodImgFileList) {
        try {
            // JSON 문자열을 RecipeRequestVo 객체로 변환
            RecipeRequestVo request = new ObjectMapper().readValue(recipeRequest, RecipeRequestVo.class);

            // 파일 리스트를 RecipeRequestVo 객체에 설정
            request.setFoodImgFileList(Arrays.asList(foodImgFileList));


            // 서비스 호출
            Long saveRecipe = recipeService.saveRecipe(
                    request.getMemberId(),
                    request.getRecipeDto(),
                    request.getCategoryPreferenceDto(),
                    request.getRecipeDetailListDto(),
                    request.getExplanations(),
                    request.getFoodImgFileList()
            );

            return ResponseEntity.ok(saveRecipe);
        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(-1L);
        }
    }
    //메인화면 구성
    @GetMapping("/main")
    public Page<MainpageRecipeDto> getMain(
            @RequestParam(required = false) Long memberId,
            RecipeSearchDto recipeSearchDto, @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (memberId != null) {
            // memberId가 존재하는 경우
            //멤버가 따로 관심사 설정 안했을경우도 만들기.
            CategoryPreferenceDto categoryPreferenceDto = new CategoryPreferenceDto();
            categoryPreferenceDto.setId(memberId);
            return recipeService.getMainForMember(categoryPreferenceDto, recipeSearchDto, pageable);
        } else {
            // memberId가 없는 경우
            return recipeService.getMain(recipeSearchDto, pageable);
        }
    }
    //세부조회(댓글 필요)
    @GetMapping("/{id}")
    public ResponseEntity<RecipeNCommentVo> viewOne(@PathVariable("id")Long recipeId){
        OneRecipeDto oneRecipeDto = recipeService.viewOne(recipeId);
        //현재 댓글은 빈 객체 돌려줌
        CommentDto commentDto = new CommentDto();//코멘트 service 구현 완료시 수정예정.
        RecipeNCommentVo recipeNCommentVo = new RecipeNCommentVo(oneRecipeDto, commentDto);
        return ResponseEntity.ok(recipeNCommentVo);
    }
    //수정
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateRecipe(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable("id")Long recipeId,
                                             @RequestParam(required = false) FoodImgDto foodImgDto,
                                             @RequestParam(required = false) List<String> explanations,
                                             @RequestParam(required = false) List<MultipartFile> foodImgFileList,
                                             @RequestBody RecipeDto recipeDto) throws IOException {
        Long userId = getUserId(userDetails);
        Long updatedRecipe = recipeService.updateRecipe(userId, recipeId, foodImgDto,explanations ,foodImgFileList, recipeDto);
        if (updatedRecipe != null) {
            return ResponseEntity.ok(updatedRecipe);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L);
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

    //삭제
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteRecipe(@PathVariable("id")Long recipeId, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        Boolean success = recipeService.deleteRecipe(userId, recipeId);

        if (success) {
            return ResponseEntity.ok("성공적으로 삭제했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제에 실패했습니다. 본인이 작성한 레시피가 맞는지 확인 부탁드립니다.");
        }
    }
}
