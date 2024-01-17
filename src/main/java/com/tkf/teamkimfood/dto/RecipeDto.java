package com.tkf.teamkimfood.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecipeDto {

    private String title;
    private String Content;
    private LocalDateTime writeDate;
    private LocalDateTime correctionDate;

    @Builder
    public RecipeDto(String title, String content, LocalDateTime writeDate, LocalDateTime correctionDate) {
        this.title = title;
        this.Content = content;
        this.writeDate = writeDate;
        this.correctionDate = correctionDate;
    }

}
