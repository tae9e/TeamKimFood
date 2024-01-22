package com.tkf.teamkimfood.controller;

import com.tkf.teamkimfood.dto.aboutrecipe.CategoryPreferenceDto;
import com.tkf.teamkimfood.dto.MainpageRecipeDto;
import com.tkf.teamkimfood.dto.aboutrecipe.RecipeRequestVo;
import com.tkf.teamkimfood.dto.aboutrecipe.RecipeSearchDto;
import com.tkf.teamkimfood.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor()
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

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
    //수정(댓글 필요)
    //삭제(댓글 필요)

}
