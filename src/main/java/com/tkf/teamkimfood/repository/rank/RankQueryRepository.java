package com.tkf.teamkimfood.repository.rank;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tkf.teamkimfood.domain.QRank;
import com.tkf.teamkimfood.domain.QRecipe;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RankQueryRepository {

    private final JPAQueryFactory queryFactory;
   @Autowired
    public RankQueryRepository(EntityManager entityManager){
       this.queryFactory = new JPAQueryFactory(entityManager);
   }

    //추천갯수 세주는 로직
    public Long recommendationTotal(Long id) {
        QRank rank = QRank.rank;
        QRecipe recipe = QRecipe.recipe;
        return queryFactory.select(Wildcard.count)
                .from(rank)
                .where(recipe.id.eq(id))
                .where(rank.recipeRecommendation.eq(true))
                .fetchOne();
    }
}
