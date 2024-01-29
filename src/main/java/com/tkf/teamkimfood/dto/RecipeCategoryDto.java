package com.tkf.teamkimfood.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeCategoryDto {

    private Long id;
    @NotEmpty(message = "어떤 분과 같이 드시나요?.")
    private String situation;
    @NotEmpty(message = "좋아하는 재료를 선택해주세요.")
    private String foodStuff;
    @NotEmpty(message = "좋아하는 음식타입을 선택해주세요.")
    private String foodNationType;

}
