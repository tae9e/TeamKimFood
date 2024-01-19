package com.tkf.teamkimfood.repository.recipe;

import com.tkf.teamkimfood.domain.RecipeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeDetailRepository extends JpaRepository<RecipeDetail, Long> {

    @Query("select rd from RecipeDetail rd where rd.recipe.id = :id")
    public List<RecipeDetail> findAllWhereRecipeId(@Param("id") Long id);
}
