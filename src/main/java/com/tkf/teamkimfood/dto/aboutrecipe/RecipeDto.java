package com.tkf.teamkimfood.dto.aboutrecipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RecipeDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime writeDate;
    private LocalDateTime correctionDate;
//    private RecipeDetailDto recipeDetailDto;
//    private CategoryPreferenceDto categoryPreferenceDto;
//    private List<FoodImgDto> foodImgDtos = new ArrayList<>();


    @JsonCreator
    public RecipeDto(@JsonProperty("title") String title, @JsonProperty("content") String content) {
        this.title = title;
        this.content = content;
    }

    @Builder
    public RecipeDto(String title, String content, LocalDateTime writeDate, LocalDateTime correctionDate, RecipeDetailDto recipeDetailDto, CategoryPreferenceDto categoryPreferenceDto, List<FoodImgDto> foodImgDtos) {
        this.title = title;
        this.content = content;
        this.writeDate = writeDate;
        this.correctionDate = correctionDate;
//        this.recipeDetailDto = recipeDetailDto;
//        this.categoryPreferenceDto = categoryPreferenceDto;
//        this.foodImgDtos = foodImgDtos;
    }

}
