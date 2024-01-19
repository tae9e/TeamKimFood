package com.tkf.teamkimfood.dto;

import com.tkf.teamkimfood.domain.RecipeDetail;
import lombok.Builder;
import lombok.Data;

@Data
public class RecipeDetailListDto {

    //재료
    private String ingredients;
    //용량
    private String dosage;

    public RecipeDetail toEntity() {
        return RecipeDetail.builder()
                .ingredients(ingredients)
                .dosage(dosage)
                .build();
    }
    public static RecipeDetailListDto toDto(RecipeDetail recipeDetail) {
        return  RecipeDetailListDto.builder()
                .ingredients(recipeDetail.getIngredients())
                .dosage(recipeDetail.getDosage())
                .build();
    }

    public RecipeDetailListDto() {
    }

    @Builder
    public RecipeDetailListDto(String ingredients, String dosage) {
        this.ingredients = ingredients;
        this.dosage = dosage;
    }
}
