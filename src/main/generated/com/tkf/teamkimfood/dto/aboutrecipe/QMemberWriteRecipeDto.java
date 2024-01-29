package com.tkf.teamkimfood.dto.aboutrecipe;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.tkf.teamkimfood.dto.aboutrecipe.QMemberWriteRecipeDto is a Querydsl Projection type for MemberWriteRecipeDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMemberWriteRecipeDto extends ConstructorExpression<MemberWriteRecipeDto> {

    private static final long serialVersionUID = 2070842008L;

    public QMemberWriteRecipeDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<Integer> viewCount, com.querydsl.core.types.Expression<String> imgUrl, com.querydsl.core.types.Expression<String> nickName) {
        super(MemberWriteRecipeDto.class, new Class<?>[]{long.class, String.class, int.class, String.class, String.class}, id, title, viewCount, imgUrl, nickName);
    }

}

