package com.tkf.teamkimfood.dto.aboutrecipe;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.tkf.teamkimfood.dto.aboutrecipe.QOneRecipeImgVo is a Querydsl Projection type for OneRecipeImgVo
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QOneRecipeImgVo extends ConstructorExpression<OneRecipeImgVo> {

    private static final long serialVersionUID = -200734540L;

    public QOneRecipeImgVo(com.querydsl.core.types.Expression<String> imgUrl, com.querydsl.core.types.Expression<String> explanation) {
        super(OneRecipeImgVo.class, new Class<?>[]{String.class, String.class}, imgUrl, explanation);
    }

}

