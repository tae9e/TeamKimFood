package com.tkf.teamkimfood.dto;

import com.tkf.teamkimfood.domain.RecipeDetail;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RecipeDetailDto {

    List<RecipeDetailListDto> recipeDetailListDtos = new ArrayList<>();

    //리스트로 받아서 리스트엔티티로 반환
    public List<RecipeDetail> ListDtoToListEntity(List<RecipeDetailListDto> recipeDetailListDtos) {
        return recipeDetailListDtos.stream()
                .map(RecipeDetailListDto::toEntity)
                .collect(Collectors.toList());
    }
}