package com.tkf.teamkimfood.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "foodimg")
public class FoodImg {

    @Id @Column(name = "foodimg_id")
    private Long id;

    private String imgName;
    private String originImgName;
    private String imgUrl;
    private String repImgYn;//대표 이미지 여부
    private String explain;

    @BatchSize(size = 100)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "foodfile_id")
    private FoodFile foodFile;

    @BatchSize(size = 100)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "magazines_id")
    private Magazine magazine;

    @BatchSize(size = 100)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @BatchSize(size = 100)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public void updateItemImg(String imgName, String originImgName, String imgUrl) {
        this.imgName = imgName;
        this.originImgName = originImgName;
        this.imgUrl = imgUrl;
    }
    //설명 추가용
    public void updateExplain(String explain) {
        this.explain = explain;
    }
    //이미지 대표사진 추가하기용
    public void setRepImgYn(String repImgYn) {
        this.repImgYn = repImgYn;
    }

    //연관관계
    public void setFoodFile(FoodFile foodFile){
        this.foodFile = foodFile;
        foodFile.getFoodImgs().add(this);
    }
    public void setMagazine(Magazine magazine){
        this.magazine=magazine;
        magazine.getFoodImgs().add(this);
    }
    public void setRecipe(Recipe recipe){
        this.recipe = recipe;
        recipe.getFoodImgs().add(this);
    }
    public void setMember(Member member) {
        this.member = member;
        member.getFoodImgs().add(this);
    }
}
