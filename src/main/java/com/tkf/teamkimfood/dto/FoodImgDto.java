package com.tkf.teamkimfood.dto;

import com.tkf.teamkimfood.domain.FoodImg;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class FoodImgDto {

    private String imgName;
    private String originImgName;
    private String imgUrl;
    private String repImgYn;//대표 이미지 여부


    //ModelMapper - 화면 DTO<=>entity 엔티티를 화면DTO로 바꿔주는 역할
    public FoodImgDto imgToDto (FoodImg itemImg) {
        FoodImgDto foodImgDto = new FoodImgDto();
        foodImgDto.imgName = itemImg.getImgName();
        foodImgDto.originImgName = itemImg.getOriginImgName();
        foodImgDto.imgUrl = itemImg.getImgUrl();
        foodImgDto.repImgYn = itemImg.getRepImgYn();
        return foodImgDto;
    }


}
