package com.tkf.teamkimfood.repository.recipe;

import com.tkf.teamkimfood.domain.prefer.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Long> {

    @Query("select rc from RecipeCategory rc where rc.recipe.id = :id")
    public RecipeCategory findWhereRecipeId(@Param("id") Long id);
}
