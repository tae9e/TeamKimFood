package com.tkf.teamkimfood.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor()
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    //레시피 저장
    @PostMapping("/save")
    public ResponseEntity<Long> saveRecipe(@RequestBody RecipeRequestVo request) {
        try {
            Long saveRecipe = recipeService.saveRecipe(
                    request.getMemberId(),
                    request.getRecipeDto(),
                    request.getCategoryPreferenceDto(),
                    request.getRecipeDetailListDto(),
                    request.getFoodImgFileList()
            );
            return ResponseEntity.ok(saveRecipe);
        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L);
        }
    }
    //메인화면 구성
    @GetMapping("/recipes")
    public Page<MainpageRecipeDto> getMain(
            @RequestParam(required = false) Long memberId,
            RecipeSearchDto recipeSearchDto, @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (memberId != null) {
            // memberId가 존재하는 경우
            CategoryPreferenceDto categoryPreferenceDto = new CategoryPreferenceDto();
            categoryPreferenceDto.setId(memberId);
            return recipeService.getMainForMember(categoryPreferenceDto, recipeSearchDto, pageable);
        } else {
            // memberId가 없는 경우
            return recipeService.getMain(recipeSearchDto, pageable);
        }
    }
    //세부조회(댓글 필요)
    @GetMapping("recipes/{id}")
    public RecipeNCommentVo viewOne(@PathVariable("id")Long recipeId){
        OneRecipeDto oneRecipeDto = recipeService.viewOne(recipeId);
        //현재 댓글은 빈 객체 돌려줌
        CommentDto commentDto = new CommentDto();//코멘트 service 구현 완료시 수정예정.
        return new RecipeNCommentVo(oneRecipeDto, commentDto);
    }
    //수정
    @PutMapping("recipes/{id}")
    public ResponseEntity<Long> updateRecipe(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable("id")Long recipeId,
                                             @RequestParam(required = false) FoodImgDto foodImgDto,
                                             @RequestParam(required = false) List<MultipartFile> foodImgFileList,
                                             @RequestBody RecipeDto recipeDto) throws IOException {
        Long userId = getUserId(userDetails);
        Long updatedRecipe = recipeService.updateRecipe(userId, recipeId, foodImgDto, foodImgFileList, recipeDto);
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
    //삭제(댓글 필요)

}
