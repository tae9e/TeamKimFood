package com.tkf.teamkimfood.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "foodimg")
public class FoodImg {

    @Id @Column(name = "foodimg_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "foodfile_id")
    private FoodFile foodFile;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "magazines_id")
    private Magazine magazine;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

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
}
