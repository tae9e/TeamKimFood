package com.tkf.teamkimfood.repository.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tkf.teamkimfood.domain.*;
import com.tkf.teamkimfood.domain.prefer.QRecipeCategory;
import com.tkf.teamkimfood.domain.prefer.RecipeCategory;
import com.tkf.teamkimfood.dto.CategoryPreferenceDto;
import com.tkf.teamkimfood.dto.MainpageRecipeDto;
import com.tkf.teamkimfood.dto.QMainpageRecipeDto;
import com.tkf.teamkimfood.dto.RecipeSearchDto;
import com.tkf.teamkimfood.dto.aboutrecipe.MemberWriteRecipeDto;
import com.tkf.teamkimfood.dto.aboutrecipe.OneRecipeDto;
import com.tkf.teamkimfood.dto.aboutrecipe.QMemberWriteRecipeDto;
import com.tkf.teamkimfood.dto.aboutrecipe.QOneRecipeDto;
import com.tkf.teamkimfood.repository.recipe.RecipeCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecipeQueryRepository implements RecipeCustomRepository{
    @PersistenceContext
    EntityManager em;

    private final JPAQueryFactory queryFactory;

    @Autowired
    public RecipeQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    //레시피 카테고리 받아온것 별로 조회
    public List<Recipe> findAllWhereRecipeCategoryOrderByWriteDateDesc(RecipeCategory recipeCategory) {
        return em.createQuery(
                        "SELECT DISTINCT r " +
                                "FROM Recipe r " +
                                "WHERE r.recipeCategory.Situation = :situation " +
                                "  OR r.recipeCategory.foodStuff = :foodStuff " +
                                "  OR r.recipeCategory.foodNationType = :foodNationType " +
                                "ORDER BY r.writeDate DESC", Recipe.class)
                .setParameter("situation", recipeCategory.getSituation())
                .setParameter("foodStuff", recipeCategory.getFoodStuff())
                .setParameter("foodNationType", recipeCategory.getFoodNationType())
                .getResultList();
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
    //한방에 다 조회(비회원용)
    @Override
    public Page<MainpageRecipeDto> getMainRecipePage(RecipeSearchDto recipeSearchDto, Pageable pageable) {
        QRecipe recipe = QRecipe.recipe;
        QFoodImg foodImg = QFoodImg.foodImg;
        QMember member = QMember.member;
        List<MainpageRecipeDto> content = queryFactory.select(
                        new QMainpageRecipeDto(
                                recipe.id,
                                recipe.title,
                                recipe.viewCount,
                                foodImg.imgUrl,
                                member.nickname
                        )
                )
                .from(recipe)
                .join(recipe.member, member)//N:1 문제 발생가능성 있음 그래서 Entity에 BatchSize(size=?)을 붙이거나 yml에 관련내용을 넣어 페이징처리
                .join(recipe.foodImgs, foodImg)//1:n이라 페이징 하면 됌
                .where(foodImg.repImgYn.eq("Y"))
                .where(recipeTitleLike(recipeSearchDto.getSearchByLike()))//null이면 실행하지 않음
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
    //1개 조회
    public Page<OneRecipeDto> getOne(Long recipeId,Pageable pageable) {
        QRecipe recipe = QRecipe.recipe;
        QFoodImg foodImg = QFoodImg.foodImg;
        QMember member = QMember.member;
        QRecipeDetail recipeDetail = QRecipeDetail.recipeDetail;
        QRecipeCategory recipeCategory = QRecipeCategory.recipeCategory;
        OneRecipeDto oneRecipeDto = queryFactory.select(
                        new QOneRecipeDto(
                                recipe.id,
                                recipe.title,
                                recipe.content,
                                recipe.viewCount,
                                recipe.writeDate,
                                recipe.correctionDate,
                                foodImg.imgUrl,
                                member.nickname,
                                recipeDetail.ingredients,
                                recipeDetail.dosage,
                                recipeCategory.Situation,
                                recipeCategory.foodStuff,
                                recipeCategory.foodNationType
                        )
                )
                .from(recipe)
                .join(recipe.member, member)
                .join(recipe.foodImgs, foodImg)
                .join(recipe.recipeDetails, recipeDetail)
                .join(recipe.recipeCategory, recipeCategory)
                .where(recipe.id.eq(recipeId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchOne();

        if (oneRecipeDto != null) {
            // 데이터가 존재하는 경우 Page 객체 생성
            return new PageImpl<>(Collections.singletonList(oneRecipeDto), pageable, 1);
        } else {
            // 데이터가 없는 경우 빈 Page 객체 생성
            return Page.empty();
        }
    }

    //내가 쓴 글 조회
    public Page<MemberWriteRecipeDto> getRecipesWriteByMemberId(Long memberId, Pageable pageable) {
        QRecipe recipe = QRecipe.recipe;
        QFoodImg foodImg = QFoodImg.foodImg;
        QMember member = QMember.member;
        List<MemberWriteRecipeDto> memberWriteRecipeDto = queryFactory.select(
                        new QMemberWriteRecipeDto(
                                recipe.id,
                                recipe.title,
                                recipe.viewCount,
                                foodImg.imgUrl,
                                member.nickname
                        )
                )
                .from(recipe)
                .join(recipe.member, member)
                .join(recipe.foodImgs, foodImg)
                .where(recipe.member.id.eq(memberId))
                .where(foodImg.repImgYn.eq("Y"))
                .orderBy(recipe.writeDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long result = queryFactory
                .select(Wildcard.count)
                .from(foodImg)
                .join(foodImg.recipe, recipe)
                .where(recipe.member.id.eq(memberId))
                .where(foodImg.repImgYn.eq("Y"))
                .fetchOne();

        return new PageImpl<>(memberWriteRecipeDto, pageable, result);
    }

    //조회수 기준으로 조회
    public Page<MainpageRecipeDto> getAllOrderByViewCount(Pageable pageable) {
        QRecipe recipe = QRecipe.recipe;
        QFoodImg foodImg = QFoodImg.foodImg;
        QMember member = QMember.member;
        List<MainpageRecipeDto> mainpageRecipeDtos = queryFactory.select(
                        new QMainpageRecipeDto(
                                recipe.id,
                                recipe.title,
                                recipe.viewCount,
                                foodImg.imgUrl,
                                member.nickname
                        )
                )
                .from(recipe)
                .join(recipe.foodImgs, foodImg)
                .join(recipe.member, member)
                .where(foodImg.repImgYn.eq("Y"))
                .orderBy(recipe.viewCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(foodImg)
                .join(foodImg.recipe, recipe)
                .where(foodImg.repImgYn.eq("Y"))
                .fetchOne();
        return new PageImpl<>(mainpageRecipeDtos, pageable, total);
    }
    //추천레시피 띄우기 로그인한 회원용 메인페이지
    public Page<MainpageRecipeDto> getAllWhereTypesOrderByWriteDay(CategoryPreferenceDto categoryPreferenceDto, RecipeSearchDto recipeSearchDto, Pageable pageable) {
        QRecipe recipe = QRecipe.recipe;
        QFoodImg foodImg = QFoodImg.foodImg;
        QMember member = QMember.member;
        QRecipeCategory recipeCategory = QRecipeCategory.recipeCategory;
        List<MainpageRecipeDto> mainpageRecipeDtos = queryFactory.selectDistinct(
                        new QMainpageRecipeDto(
                                recipe.id,
                                recipe.title,
                                recipe.viewCount,
                                foodImg.imgUrl,
                                member.nickname
                        )
                )
                .from(recipe)
                .join(recipe.member, member)
                .join(recipe.foodImgs, foodImg)
                .join(recipe.recipeCategory, recipeCategory)
                .where(foodImg.repImgYn.eq("Y"))
                .where(recipeTitleLike(recipeSearchDto.getSearchByLike()))
                .where(recipeCategory.Situation.eq(categoryPreferenceDto.getSituation())
                        .or(recipeCategory.foodStuff.eq(categoryPreferenceDto.getFoodStuff())
                                .or(recipeCategory.foodNationType.eq(categoryPreferenceDto.getFoodNationType()))))
                .orderBy(recipe.writeDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(Wildcard.count)
                .from(foodImg)
                .join(foodImg.recipe, recipe)
                .where(foodImg.repImgYn.eq("Y"))
                .fetchOne();
        return new PageImpl<>(mainpageRecipeDtos, pageable, total);
    }

}
