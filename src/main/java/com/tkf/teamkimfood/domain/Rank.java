package com.tkf.teamkimfood.domain;

import com.tkf.teamkimfood.domain.status.RankSearchStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private boolean recommendation = false;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    //검색관련 데이터주입
    @Builder
    public Rank(RankSearchStatus rankSearchStatus) {
        this.rankSearchStatus = rankSearchStatus;
    }

    //연관관계 메서드(확인 필요)
}
