package com.tkf.teamkimfood.dto;

import com.tkf.teamkimfood.dto.aboutrecipe.OneRecipeDto;
import lombok.Data;

@Data
public class RecipeNCommentVo {

    private OneRecipeDto oneRecipeDto;
    private CommentRequestDto commentDto;

    public RecipeNCommentVo(OneRecipeDto oneRecipeDto, CommentRequestDto commentDto) {
        this.oneRecipeDto = oneRecipeDto;
        this.commentDto = commentDto;
    }
}
