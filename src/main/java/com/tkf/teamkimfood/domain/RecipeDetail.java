package com.tkf.teamkimfood.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

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

    @BatchSize(size = 100)
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
