package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.Rank;
import com.tkf.teamkimfood.domain.Recipe;
import com.tkf.teamkimfood.dto.ranks.RankDto;
import com.tkf.teamkimfood.repository.MemberRepository;
import com.tkf.teamkimfood.repository.rank.RankQueryRepository;
import com.tkf.teamkimfood.repository.rank.RankRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankService {
    //조회수 관련은 RecipeService에서 했습니다.
    //recipe 추천수에따른 조회, 추천수 증가 감소 로직
    //멤버,레시피 사이에 둘을 잇는 엔티티 새로 만들어야함.
    //멤버가 해당 레시피에 대한 추천은 default는 false 프론트에서 추천을 누를시 값이 true 로 변경
    //이미 해당 레시피에 추천을 준 경우 값을 false로 변경
    //레시피, 멤버와는 1:N이 될 것(생성될 엔티티가)
    //멤버는 여러 게시글에 추천을 줄 수 있다. 한 게시글엔 1개의 추천만 줄 수 있다. 레시피는 여러명의 멤버에게 추천을 받을 수 있다.
    //랭킹 엔티티엔 boolean으로 추천 1개만 넣고, 추천수는 Wildcard.count, .where(entity.active.eq(isActive))로 하면 될 것.

    private final RankRepository rankRepository;
    private final RankQueryRepository rankQueryRepository;
    private final MemberRepository memberRepository;
    private final RecipeRepository recipeRepository;

    //랭크 생성후 추천 증감시키는 로직
    public Long recommRecipeVariation(Long memberId, Long recipeId, Rank rank) {
        Member member = memberRepository.findById(memberId).orElseThrow(NullPointerException::new);
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(NullPointerException::new);

        rank.setMember(member);
        rank.setRecipe(recipe);
        Rank saved = rankRepository.save(rank);
        RankDto rankDto = new RankDto();
        rankDto.setId(saved.getId());
        rankDto.setRecipeRecommendation(saved.isRecipeRecommendation());
        rankDto.setUserRecommendation(saved.isUserRecommendation());

        //추천을 주기 위해서
        if (rankDto.isRecipeRecommendation()) {
            rankDto.setRecipeRecommendation(true);
        } else {
            rankDto.setRecipeRecommendation(false);
        }
        rank.recipeRecommend(rankDto);//추천수 증감시킴
        rankRepository.save(rank);
        return rankQueryRepository.recommendationTotal(rank.getId());
    }
    //A가 B를 추천하는 상황.
}
