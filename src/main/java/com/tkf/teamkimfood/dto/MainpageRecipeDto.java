package com.tkf.teamkimfood.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import lombok.Builder;
import lombok.Data;

//메인에서 표시할 정보 담기
@Data
public class MainpageRecipeDto {

    private Long id;
    private String title;
    private int viewCount;
    private String imgUrl;
    private String nickName;
    //용법
    private String ingredients;
    //용량
    private String dosage;
    private String Situation;//상황 : 혼밥,연인, 가족 등등
    private String foodStuff;//음식재료 : 육류 어류 등등
    private String foodNationType;//음식타입 : 한식 중식 일식 등

    //메인페이지에서 조회
    @QueryProjection
    public MainpageRecipeDto(Long id, String title, int viewCount, String imgUrl, String nickName) {
        this.id = id;
        this.title = title;
        this.viewCount = viewCount;
        this.imgUrl = imgUrl;
        this.nickName = nickName;
    }
}
