package com.tkf.teamkimfood.dto.aboutrecipe;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MemberWriteRecipeDto {
    private Long id;
    private String title;
    private int viewCount;
    private String imgUrl;
    private String nickName;

    @QueryProjection
    public MemberWriteRecipeDto(Long id, String title, int viewCount, String imgUrl, String nickName) {
        this.id = id;
        this.title = title;
        this.viewCount = viewCount;
        this.imgUrl = imgUrl;
        this.nickName = nickName;
    }
}
