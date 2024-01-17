package com.tkf.teamkimfood.repository.query;

import com.tkf.teamkimfood.domain.Recipe;
import com.tkf.teamkimfood.domain.prefer.RecipeCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RecipeQueryRepository {
    @PersistenceContext
    EntityManager em;

    public List<Recipe> findAllWhereMemberId(Long id) {
        return em.createQuery(
                "select r "+
                        "from Recipe r "+
                        "where r.member = "+id
        , Recipe.class).getResultList();
    }

    public List<Recipe> findAllWhereRecipeCategoryOrderByWriteDateDesc(RecipeCategory recipeCategory) {
        return em.createQuery(
                        "SELECT DISTINCT r " +
                                "FROM Recipe r " +
                                "WHERE r.recipeCategory = :recipeCategory " +
                                "ORDER BY r.writeDate DESC", Recipe.class)
                .setParameter("recipeCategory", recipeCategory)
                .getResultList();
    }
}
