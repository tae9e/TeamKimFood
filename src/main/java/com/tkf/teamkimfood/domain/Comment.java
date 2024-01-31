package com.tkf.teamkimfood.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "comments")
public class Comment {

    @Id @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(max = 300)
    private String content;

    @Column
    @CreatedDate
    private LocalDateTime commentDate;

    @Column
    @LastModifiedDate
    private LocalDateTime correctionDate;

    @BatchSize(size = 100)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "nickname")
//    private String nickname;

    @BatchSize(size = 100)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

//    @OneToMany
//    private List<Comment> comments = new ArrayList<>();

    //코멘트 데이터 넣을 때
    @Builder
    public Comment(Long id, String content, LocalDateTime commentDate, LocalDateTime correctionDate, Member member, Recipe recipe) {
        this.id = id;
        this.content = content;
        this.commentDate = commentDate;
        this.correctionDate = correctionDate;
        this.member = member;
        this.recipe = recipe;
    }

    public void update(String content){
        this.content = content;
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
