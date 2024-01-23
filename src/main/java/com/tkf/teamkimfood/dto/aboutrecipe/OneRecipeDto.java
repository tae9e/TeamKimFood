package com.tkf.teamkimfood.dto.aboutrecipe;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OneRecipeDto {
    private Long id;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime writeDate;
    private LocalDateTime correctionDate;

    private String imgUrl;

    private String nickName;
    //용법
    private String ingredients;
    //용량
    private String dosage;
    private String situation;//상황 : 혼밥,연인, 가족 등등
    private String foodStuff;//음식재료 : 육류 어류 등등
    private String foodNationType;//음식타입 : 한식 중식 일식 등
    private String explanation;

    //1개 조회용
    @QueryProjection
    public OneRecipeDto(Long id, String title, String content, int viewCount, LocalDateTime writeDate, LocalDateTime correctionDate, String imgUrl, String explanation, String nickName, String ingredients, String dosage, String situation, String foodStuff, String foodNationType) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.writeDate = writeDate;
        this.correctionDate = correctionDate;
        this.imgUrl = imgUrl;
        this.explanation = explanation;
        this.nickName = nickName;
        this.ingredients = ingredients;
        this.dosage = dosage;
        this.situation = situation;
        this.foodStuff = foodStuff;
        this.foodNationType = foodNationType;
    }


}
