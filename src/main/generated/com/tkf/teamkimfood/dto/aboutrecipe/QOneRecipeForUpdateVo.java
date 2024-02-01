package com.tkf.teamkimfood.dto.aboutrecipe;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.tkf.teamkimfood.dto.aboutrecipe.QOneRecipeForUpdateVo is a Querydsl Projection type for OneRecipeForUpdateVo
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QOneRecipeForUpdateVo extends ConstructorExpression<OneRecipeForUpdateVo> {

    private static final long serialVersionUID = 1930499619L;

    public QOneRecipeForUpdateVo(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<Integer> viewCount, com.querydsl.core.types.Expression<java.time.LocalDateTime> writeDate, com.querydsl.core.types.Expression<java.time.LocalDateTime> correctionDate, com.querydsl.core.types.Expression<String> nickName, com.querydsl.core.types.Expression<String> situation, com.querydsl.core.types.Expression<String> foodStuff, com.querydsl.core.types.Expression<String> foodNationType, com.querydsl.core.types.Expression<Long> memberId) {
        super(OneRecipeForUpdateVo.class, new Class<?>[]{long.class, String.class, String.class, int.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class, String.class, String.class, String.class, String.class, long.class}, id, title, content, viewCount, writeDate, correctionDate, nickName, situation, foodStuff, foodNationType, memberId);
    }

}

