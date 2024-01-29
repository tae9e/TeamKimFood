package com.tkf.teamkimfood.dto;

import com.tkf.teamkimfood.dto.aboutrecipe.RecipeDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MemberDto {

    @Getter
    @Setter
    private List<RecipeDto> myPostsList;
}
