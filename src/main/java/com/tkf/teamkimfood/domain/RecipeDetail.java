package com.tkf.teamkimfood.domain;

import com.tkf.teamkimfood.dto.RecipeDetailDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "recipe_detail")
public class RecipeDetail {

    @Id @Column(name = "recipeDtl_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //재료
    private String ingredients;
    //용량
    private String dosage;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    //값주입 이걸로
    @Builder
    public RecipeDetail(String ingredients, String dosage) {
        this.ingredients = ingredients;
        this.dosage = dosage;
    }

    public void setRecipe (Recipe recipe) {
        this.recipe = recipe;
        recipe.getRecipeDetails().add(this);
    }

}
