package com.tkf.teamkimfood.dto.ranks;

import com.querydsl.core.annotations.QueryProjection;
import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.Recipe;
import com.tkf.teamkimfood.domain.status.RankSearchStatus;
import lombok.Data;

@Data
public class RankDto {

    private Long id;
    private RankSearchStatus rankSearchStatus;
    //레시피 추천 관련 유저가 가지고 있는 값
    private boolean recipeRecommendation = false;
    //유저 추천 관련 해당 유저에 대한 값
    private boolean userRecommendation = false;
    private Long memberId;
    private Long recipeId;

    public RankDto() {
    }

    public RankDto(Long memberId, Long recipeId) {
        this.memberId = memberId;
        this.recipeId = recipeId;
    }

    @QueryProjection
    public RankDto(Long id, RankSearchStatus rankSearchStatus, boolean recipeRecommendation, boolean userRecommendation) {
        this.id = id;
        this.rankSearchStatus = rankSearchStatus;
        this.recipeRecommendation = recipeRecommendation;
        this.userRecommendation = userRecommendation;
    }
}
