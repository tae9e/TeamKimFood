package com.tkf.teamkimfood.domain;

import com.tkf.teamkimfood.domain.status.RankSearchStatus;
import com.tkf.teamkimfood.dto.ranks.RankDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ranks")
@Getter
public class Rank {

    @Id @Column(name = "rank_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RankSearchStatus rankSearchStatus;

    //레시피 추천 관련 유저가 가지고 있는 값
    private boolean recipeRecommendation = false;
    //유저 추천 관련 해당 유저에 대한 값
    private boolean userRecommendation = false;

    @BatchSize(size = 100)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @BatchSize(size = 100)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    //검색관련 데이터주입

    //연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getRank().add(this);
    }
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        recipe.getRank().add(this);
    }

    public void recipeRecommend(RankDto rankDto) {
        this.recipeRecommendation = rankDto.isRecipeRecommendation();
    }
    public void memberRecommend(RankDto rankDto) {
        this.userRecommendation = rankDto.isUserRecommendation();
    }

    @Builder
    public Rank(Long id, RankSearchStatus rankSearchStatus, boolean recipeRecommendation, boolean userRecommendation) {
        this.id = id;
        this.rankSearchStatus = rankSearchStatus;
        this.recipeRecommendation = recipeRecommendation;
        this.userRecommendation = userRecommendation;
    }
}
