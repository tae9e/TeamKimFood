package com.tkf.teamkimfood.dto.aboutrecipe;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class OneRecipeImgVo {

    private String imgUrl;
    private String explanation;

    @QueryProjection
    public OneRecipeImgVo(String imgUrl, String explanation) {
        this.imgUrl = imgUrl;
        this.explanation = explanation;
    }
}
