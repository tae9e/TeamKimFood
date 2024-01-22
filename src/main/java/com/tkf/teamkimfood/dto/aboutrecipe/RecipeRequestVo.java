package com.tkf.teamkimfood.dto.aboutrecipe;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RecipeRequestVo {

    private Long memberId;
    private RecipeDto recipeDto;
    private CategoryPreferenceDto categoryPreferenceDto;
    private List<RecipeDetailListDto> recipeDetailListDto;
    private List<MultipartFile> foodImgFileList;

}
