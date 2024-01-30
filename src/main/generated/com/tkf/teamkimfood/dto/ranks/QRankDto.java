package com.tkf.teamkimfood.dto.ranks;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.tkf.teamkimfood.dto.ranks.QRankDto is a Querydsl Projection type for RankDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QRankDto extends ConstructorExpression<RankDto> {

    private static final long serialVersionUID = 557017395L;

    public QRankDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<com.tkf.teamkimfood.domain.status.RankSearchStatus> rankSearchStatus, com.querydsl.core.types.Expression<Boolean> recipeRecommendation, com.querydsl.core.types.Expression<Boolean> userRecommendation) {
        super(RankDto.class, new Class<?>[]{long.class, com.tkf.teamkimfood.domain.status.RankSearchStatus.class, boolean.class, boolean.class}, id, rankSearchStatus, recipeRecommendation, userRecommendation);
    }

}

