package com.tkf.teamkimfood.dto.aboutrecipe;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.tkf.teamkimfood.dto.aboutrecipe.QOneRecipeIngDoVo is a Querydsl Projection type for OneRecipeIngDoVo
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QOneRecipeIngDoVo extends ConstructorExpression<OneRecipeIngDoVo> {

    private static final long serialVersionUID = 395731070L;

    public QOneRecipeIngDoVo(com.querydsl.core.types.Expression<String> ingredients, com.querydsl.core.types.Expression<String> dosage) {
        super(OneRecipeIngDoVo.class, new Class<?>[]{String.class, String.class}, ingredients, dosage);
    }

}

