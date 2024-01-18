package com.tkf.teamkimfood.repository.query;

import com.tkf.teamkimfood.domain.Recipe;
import com.tkf.teamkimfood.domain.prefer.RecipeCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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
                        "where r.member = :id "+
                        "order by r.writeDate desc "
        , Recipe.class).setParameter("id", id)
                .getResultList();
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
    //받아온 멤버아이디와 레시피아이디가 일치하는 레시피
    public Recipe findOneWhereMemberIdAndRecipeId(Long memberId, Long recipeId) {
        TypedQuery<Recipe> query = em.createQuery(
                "select r " +
                        "from Recipe r " +
                        "where r.member.id = :memberId and r.id = :recipeId", Recipe.class
        );

        query.setParameter("memberId", memberId);
        query.setParameter("recipeId", recipeId);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            // 해당하는 결과가 없을 경우 예외 처리
            return null;
        }
    }
}
