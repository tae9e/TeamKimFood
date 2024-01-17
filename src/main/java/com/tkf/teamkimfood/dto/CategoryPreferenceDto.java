package com.tkf.teamkimfood.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CategoryPreferenceDto {

    private String Situation;
    private String foodStuff;
    private String foodNationType;

    @Builder
    public CategoryPreferenceDto(String situation, String foodStuff, String foodNationType) {
        this.Situation = situation;
        this.foodStuff = foodStuff;
        this.foodNationType = foodNationType;
    }

}
