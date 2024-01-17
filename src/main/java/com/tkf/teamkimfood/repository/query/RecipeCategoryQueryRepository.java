package com.tkf.teamkimfood.repository.query;

import com.tkf.teamkimfood.domain.prefer.RecipeCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RecipeCategoryQueryRepository {

    @PersistenceContext
    EntityManager em;

    public Long save(RecipeCategory recipeCategory) {
        em.persist(recipeCategory);
        return recipeCategory.getId();
    }

    //해당 레시피 아이디와 연관된 모든 멤버레시피 가져오기
    public RecipeCategory findWithRecipe(RecipeCategory recipeCategory) {
        return em.createQuery(
                "select rc "+
                        "from RecipeCategory rc "+
                        "where rc.recipe = :id", RecipeCategory.class
        ).setParameter("id",recipeCategory.getRecipe()).getSingleResult();
    }
}
