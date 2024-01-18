package com.tkf.teamkimfood.repository.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tkf.teamkimfood.domain.QFoodImg;
import com.tkf.teamkimfood.domain.QMember;
import com.tkf.teamkimfood.domain.QRecipe;
import com.tkf.teamkimfood.domain.Recipe;
import com.tkf.teamkimfood.domain.prefer.RecipeCategory;
import com.tkf.teamkimfood.dto.MainpageRecipeDto;
import com.tkf.teamkimfood.dto.QMainpageRecipeDto;
import com.tkf.teamkimfood.dto.RecipeSearchDto;
import com.tkf.teamkimfood.repository.recipe.RecipeCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecipeQueryRepository implements RecipeCustomRepository{
    @PersistenceContext
    EntityManager em;

    private JPAQueryFactory queryFactory;

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
    private BooleanExpression recipeTitleLike(String searchQuery){
        return searchQuery == null ? null : QRecipe.recipe.title.like("%" + searchQuery + "%");
    }
    //의도치않게 서치를 해버렸습니다...
    @Override
    public Page<MainpageRecipeDto> getMainRecipePage(RecipeSearchDto recipeSearchDto, Pageable pageable) {
        QRecipe recipe = QRecipe.recipe;
        QFoodImg foodImg = QFoodImg.foodImg;
        QMember member = QMember.member;
        List<MainpageRecipeDto> content = queryFactory.select(
                        new QMainpageRecipeDto(
                                recipe.title,
                                recipe.viewCount,
                                foodImg.imgUrl,
                                member.nickname)
                )
                .from(foodImg)
                .join(foodImg.recipe, recipe)
                .where(foodImg.repImgYn.eq("Y"))
                .where(recipeTitleLike(recipeSearchDto.getSearchByLike()))
                .orderBy(recipe.writeDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(foodImg)
                .join(foodImg.recipe, recipe)
                .where(foodImg.repImgYn.eq("Y"))
                .where(recipeTitleLike(recipeSearchDto.getSearchByLike()))
                .fetchOne();
        //조회된 결과(content), 페이징 정보(pageable), 총 결과의 개수(total)를 이용하여 Page<MainpageRecipeDto>를 반환
        return new PageImpl<>(content, pageable, total);
    }
}
