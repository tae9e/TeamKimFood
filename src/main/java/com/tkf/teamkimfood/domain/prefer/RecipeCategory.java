package com.tkf.teamkimfood.domain.prefer;

import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.Recipe;
import com.tkf.teamkimfood.dto.RecipeCategoryDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mpreference_id")
    private Long id;
    private String Situation;//상황 : 혼밥,연인, 가족 등등
    private String foodStuff;//음식재료 : 육류 어류 등등
    private String foodNationType;//음식타입 : 한식 중식 일식 등

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Builder
    public RecipeCategory(String situation, String foodStuff, String foodNationType, Recipe recipe) {
        this.Situation = situation;
        this.foodStuff = foodStuff;
        this.foodNationType = foodNationType;
        this.recipe = recipe;
    }


    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
