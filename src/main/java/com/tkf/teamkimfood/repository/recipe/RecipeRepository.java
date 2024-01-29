package com.tkf.teamkimfood.repository.recipe;

import com.tkf.teamkimfood.domain.Recipe;
import com.tkf.teamkimfood.dto.dashboards.RecipeManagementDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {


    @Query("UPDATE Recipe r SET r.viewCount = r.viewCount + 1 WHERE r.id = :recipeId")
   public Recipe addViewCount(@Param("recipeId") Long recipeId);

    @Query("select new com.tkf.teamkimfood.dto.dashboards.RecipeManagementDto(r.id, r.title, m.nickname, r.viewCount, r.writeDate) " +
            "from Recipe r join r.member m ORDER BY r.writeDate desc ")
    public List<RecipeManagementDto> findAllByMemberNickNameFromRecipe();
}
