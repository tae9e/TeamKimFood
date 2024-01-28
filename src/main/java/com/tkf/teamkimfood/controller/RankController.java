package com.tkf.teamkimfood.controller;

import com.tkf.teamkimfood.dto.MainpageRecipeDto;
import com.tkf.teamkimfood.dto.MemberScoreTotalDto;
import com.tkf.teamkimfood.service.RankService;
import com.tkf.teamkimfood.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class RankController {

    private final RankService rankService;
    private final RecipeService recipeService;

    //추천수 기반 랭크 조회(레시피)
    @GetMapping("/api/rank/recipeRecommend")
    public Page<MainpageRecipeDto> seeRecipeOrderByTotalRecommend(@RequestParam(defaultValue = "0")int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return recipeService.getAllOrderByRankPoint(pageable);
    }
    //조회수 기반 랭크 조회(레시피)
    @GetMapping("/api/recipe/recipeTotalView")
    public Page<MainpageRecipeDto> seeRecipeOrderByViewCount(@RequestParam(defaultValue = "0")int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return recipeService.findAllOrderByViewCount(pageable);
    }
    //유저가 가진 레시피의 추천수 총합 기준 랭크 조회
    @GetMapping("/api/rank/users")
    public List<MemberScoreTotalDto> seeUserOrderByTotalScore() {
        return rankService.memberScoreTotal();
    }


}
