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

    //회원이 자기가 쓴 글 조회
    public List<Recipe> findAllWhereMemberId(Long id) {
        return em.createQuery(
                "select r "+
                        "from Recipe r "+
                        "where r.member = "+id
        , Recipe.class).getResultList();
    }
    //레시피 카테고리 받아온것 별로 조회(근데 3개가 정확히 일치되는것만 불러오는거일수도 있음)
    public List<Recipe> findAllWhereRecipeCategoryOrderByWriteDateDesc(RecipeCategory recipeCategory) {
        return em.createQuery(
                        "SELECT DISTINCT r " +
                                "FROM Recipe r " +
                                "WHERE r.recipeCategory = :recipeCategory " +
                                "ORDER BY r.writeDate DESC", Recipe.class)
                .setParameter("recipeCategory", recipeCategory)
                .getResultList();
    }
    //조회수 순으로 출력
    public List<Recipe> findAllOrderByViewCount(){
        return em.createQuery(
                "select r " +
                        "from Recipe r " +
                        "order by r.viewCount desc", Recipe.class
        ).getResultList();
    }
}
