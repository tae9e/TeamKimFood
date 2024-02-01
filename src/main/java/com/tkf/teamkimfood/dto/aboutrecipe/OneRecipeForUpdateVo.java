package com.tkf.teamkimfood.dto.aboutrecipe;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OneRecipeForUpdateVo {
    private Long memberId;
    private Long id;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime writeDate;
    private LocalDateTime correctionDate;


    private String nickName;
    //용법
    private String situation;//상황 : 혼밥,연인, 가족 등등
    private String foodStuff;//음식재료 : 육류 어류 등등
    private String foodNationType;//음식타입 : 한식 중식 일식 등


    //1개 조회용
    @QueryProjection
    public OneRecipeForUpdateVo(Long id, String title, String content, int viewCount, LocalDateTime writeDate, LocalDateTime correctionDate, String nickName, String situation, String foodStuff, String foodNationType, Long memberId) {
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
}
