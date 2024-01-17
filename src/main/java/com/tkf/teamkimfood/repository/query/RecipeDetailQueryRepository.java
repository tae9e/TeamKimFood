package com.tkf.teamkimfood.repository.query;

import com.tkf.teamkimfood.domain.RecipeDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecipeDetailQueryRepository {

    @PersistenceContext
    EntityManager em;

    public List<RecipeDetail> findOneWhereRecipeId(RecipeDetail recipeDetail) {
        return em.createQuery(
                "select rd "+
                        "from RecipeDetail rd "+
                        "where rd.recipe = :id", RecipeDetail.class
        ).setParameter("id", recipeDetail.getRecipe().getId())
                .getResultList();
    }
}
