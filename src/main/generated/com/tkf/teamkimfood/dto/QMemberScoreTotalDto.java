package com.tkf.teamkimfood.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.tkf.teamkimfood.dto.QMemberScoreTotalDto is a Querydsl Projection type for MemberScoreTotalDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMemberScoreTotalDto extends ConstructorExpression<MemberScoreTotalDto> {

    private static final long serialVersionUID = -2099644614L;

    public QMemberScoreTotalDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<Long> totalScore) {
        super(MemberScoreTotalDto.class, new Class<?>[]{long.class, String.class, long.class}, id, nickname, totalScore);
    }

}

