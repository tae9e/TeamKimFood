package com.tkf.teamkimfood.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "comments")
public class Comment {

    @Id @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDateTime commentDate;
    private LocalDateTime correctionDate;

    @BatchSize(size = 100)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @BatchSize(size = 100)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    //코멘트 데이터 넣을 때
    @Builder
    public Comment(String content, LocalDateTime commentDate, LocalDateTime correctionDate) {
        this.content = content;
        this.commentDate = commentDate;
        this.correctionDate = correctionDate;
    }

    //연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getComments().add(this);
    }
    public void setRecipe(Recipe recipe){
        this.recipe = recipe;
        recipe.getComments().add(this);
    }

}
