package com.tkf.teamkimfood.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class RecipeDto {

    private String title;
    private String Content;
    private LocalDateTime writeDate;
    private LocalDateTime correctionDate;
    private RecipeDetailDto recipeDetailDto;
    private CategoryPreferenceDto categoryPreferenceDto;
    private List<FoodImgDto> foodImgDtos = new ArrayList<>();

    @Builder
    public RecipeDto(String title, String content, LocalDateTime writeDate, LocalDateTime correctionDate, RecipeDetailDto recipeDetailDto, CategoryPreferenceDto categoryPreferenceDto, List<FoodImgDto> foodImgDtos) {
        this.title = title;
        this.Content = content;
        this.writeDate = writeDate;
        this.correctionDate = correctionDate;
        this.recipeDetailDto = recipeDetailDto;
        this.categoryPreferenceDto = categoryPreferenceDto;
        this.foodImgDtos = foodImgDtos;
    }

}
