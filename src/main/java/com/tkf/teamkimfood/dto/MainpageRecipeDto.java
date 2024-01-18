package com.tkf.teamkimfood.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import lombok.Builder;
import lombok.Data;

//메인에서 표시할 정보 담기
@Data
public class MainpageRecipeDto {

    private String title;
    private int viewCount;
    private String imgUrl;
    private String nickName;

    @QueryProjection
    public MainpageRecipeDto(String title, int viewCount, String imgUrl, String nickName) {
        this.title = title;
        this.viewCount = viewCount;
        this.imgUrl = imgUrl;
        this.nickName = nickName;
    }

    public MainpageRecipeDto() {
    }
}
