package com.tkf.teamkimfood.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CategoryPreferenceDto {

    private Long id;
    private String Situation;
    private String foodStuff;
    private String foodNationType;

    public CategoryPreferenceDto() {
    }

    @Builder
    public CategoryPreferenceDto(Long id,String situation, String foodStuff, String foodNationType) {
        this.id = id;
        this.Situation = situation;
        this.foodStuff = foodStuff;
        this.foodNationType = foodNationType;
    }

}
