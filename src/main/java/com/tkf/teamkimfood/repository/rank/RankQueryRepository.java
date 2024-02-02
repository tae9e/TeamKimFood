package com.tkf.teamkimfood.repository.rank;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tkf.teamkimfood.domain.QMember;
import com.tkf.teamkimfood.domain.QRank;
import com.tkf.teamkimfood.domain.QRecipe;
import com.tkf.teamkimfood.dto.MemberScoreTotalDto;
import com.tkf.teamkimfood.dto.QMemberScoreTotalDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        return queryFactory.select(Wildcard.count)
                .from(rank)
                .where(rank.recipe.id.eq(id))
                .where(rank.recipeRecommendation.eq(true))
                .fetchOne();
    }
    //멤버 레시피별 총 받은 추천수 합계산 후 멤버전체의 레시피별 desc
    public List<MemberScoreTotalDto> memberScoreTotal() {
        QRank rank = QRank.rank;
        QMember member = QMember.member;
        QRecipe recipe = QRecipe.recipe;

        return queryFactory.select(
                        new QMemberScoreTotalDto(
                                member.id,
                                member.nickname,
                                rank.recipeRecoTotal.sum().as("totalScore")
                        )
                )
                .from(rank)
                .join(rank.recipe, recipe)
                .join(rank.member, member)
                .groupBy(rank.member.id, member.nickname)
                .orderBy(rank.recipeRecoTotal.sum().desc())
                .fetch();
    }
}
