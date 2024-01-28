package com.tkf.teamkimfood.dto.aboutrecipe;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OneRecipeDto {
    private Long memberId;
    private Long id;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime writeDate;
    private LocalDateTime correctionDate;

    private List<String> imgUrls;

    private String nickName;
    //용법
    private List<String> ingredients;
    //용량
    private List<String> dosage;
    private String situation;//상황 : 혼밥,연인, 가족 등등
    private String foodStuff;//음식재료 : 육류 어류 등등
    private String foodNationType;//음식타입 : 한식 중식 일식 등
    private List<String> explanations;

    //1개 조회용
    @QueryProjection
    public OneRecipeDto(Long id, String title, String content, int viewCount, LocalDateTime writeDate, LocalDateTime correctionDate, String nickName, String situation, String foodStuff, String foodNationType, Long memberId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.writeDate = writeDate;
        this.correctionDate = correctionDate;
        this.nickName = nickName;
        this.situation = situation;
        this.foodStuff = foodStuff;
        this.foodNationType = foodNationType;
        this.memberId = memberId;
    }

    public void insertRecipes(List<OneRecipeImgVo> oneRecipeImgVos) {
        oneRecipeImgVos.forEach(oiv->{
            this.imgUrls.add(oiv.getImgUrl());
            this.explanations.add(oiv.getExplanation());
        });
    }
    public void insertIngreDosage(List<OneRecipeIngDoVo> oneRecipeIngDoVos) {
        oneRecipeIngDoVos.forEach(oriv->{
            this.ingredients.add(oriv.getIngredients());
            this.dosage.add(oriv.getDosage());
        });
    }


}
