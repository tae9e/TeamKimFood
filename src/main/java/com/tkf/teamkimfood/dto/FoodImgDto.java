package com.tkf.teamkimfood.dto;

import com.tkf.teamkimfood.domain.FoodImg;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FoodImgDto {

    private String imgName;
    private String originImgName;
    private String imgUrl;
    private String repImgYn;//대표 이미지 여부
    private List<Long> foodImgIds = new ArrayList<>();

    public static FoodImgDto imgToDto (FoodImg foodImg) {
        FoodImgDto foodImgDto = new FoodImgDto();
        foodImgDto.imgName = foodImg.getImgName();
        foodImgDto.originImgName = foodImg.getOriginImgName();
        foodImgDto.imgUrl = foodImg.getImgUrl();
        foodImgDto.repImgYn = foodImg.getRepImgYn();
        return foodImgDto;
    }


}
