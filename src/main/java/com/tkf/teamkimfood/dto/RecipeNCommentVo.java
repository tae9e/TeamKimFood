package com.tkf.teamkimfood.dto;

import com.tkf.teamkimfood.dto.aboutrecipe.OneRecipeDto;
import com.tkf.teamkimfood.dto.aboutrecipe.OneRecipeImgVo;
import com.tkf.teamkimfood.dto.aboutrecipe.OneRecipeIngDoVo;
import lombok.Data;

import java.util.List;

@Data
public class RecipeNCommentVo {

    private OneRecipeDto oneRecipeDto;
    private CommentDto commentDto;
    List<OneRecipeImgVo> oneRecipeImgVos;
    List<OneRecipeIngDoVo> oneRecipeIngDoVos;
    Boolean equalMember = false;

    public RecipeNCommentVo(OneRecipeDto oneRecipeDto, CommentDto commentDto, List<OneRecipeImgVo> oneRecipeImgVos, List<OneRecipeIngDoVo> oneRecipeIngDoVos) {
        this.oneRecipeDto = oneRecipeDto;
        this.commentDto = commentDto;
        this.oneRecipeImgVos = oneRecipeImgVos;
        this.oneRecipeIngDoVos = oneRecipeIngDoVos;
    }
}
