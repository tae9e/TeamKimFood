package com.tkf.teamkimfood.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.tkf.teamkimfood.dto.QMainpageRecipeDto is a Querydsl Projection type for MainpageRecipeDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMainpageRecipeDto extends ConstructorExpression<MainpageRecipeDto> {

    private static final long serialVersionUID = -918432944L;

    public QMainpageRecipeDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<Integer> viewCount, com.querydsl.core.types.Expression<String> imgUrl, com.querydsl.core.types.Expression<String> nickName, com.querydsl.core.types.Expression<java.time.LocalDateTime> writeDate) {
        super(MainpageRecipeDto.class, new Class<?>[]{long.class, String.class, int.class, String.class, String.class, java.time.LocalDateTime.class}, id, title, viewCount, imgUrl, nickName, writeDate);
    }

}

