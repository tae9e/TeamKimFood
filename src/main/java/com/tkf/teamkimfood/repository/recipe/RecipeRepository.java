package com.tkf.teamkimfood.repository.recipe;

import com.tkf.teamkimfood.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {


    @Query("UPDATE Recipe r SET r.viewCount = r.viewCount + 1 WHERE r.id = :recipeId")
   public void addViewCount(@Param("recipeId") Long recipeId);
}
