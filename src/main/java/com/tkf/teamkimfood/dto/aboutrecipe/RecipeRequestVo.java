package com.tkf.teamkimfood.dto.aboutrecipe;

import com.tkf.teamkimfood.domain.status.MemberRole;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RecipeRequestVo {

    private Long memberId;
    private MemberRole memberRole;
    private RecipeDto recipeDto;
    private CategoryPreferenceDto categoryPreferenceDto;
    private List<RecipeDetailListDto> recipeDetailListDto;
    private List<String> explanations;
    private List<MultipartFile> foodImgFileList;

}
