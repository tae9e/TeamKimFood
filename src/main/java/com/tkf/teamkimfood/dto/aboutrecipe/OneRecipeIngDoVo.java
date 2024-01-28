package com.tkf.teamkimfood.dto.aboutrecipe;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class OneRecipeIngDoVo {
    private String ingredients;
    private String dosage;

    @QueryProjection
    public OneRecipeIngDoVo(String ingredients, String dosage) {
        this.ingredients = ingredients;
        this.dosage = dosage;
    }
}
