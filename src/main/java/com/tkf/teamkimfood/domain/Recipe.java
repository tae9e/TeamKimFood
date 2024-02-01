package com.tkf.teamkimfood.domain;

import com.tkf.teamkimfood.domain.prefer.RecipeCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class Recipe {

    @Id @Column(name = "recipe_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    private int viewCount;//디폴트를 지웠습니다.

    @ColumnDefault("0")
    private long score;

    private LocalDateTime writeDate;
    private LocalDateTime correctionDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    private List<RecipeDetail> recipeDetails = new ArrayList<>();

//    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
//    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
//    @Where(clause = "parent_id is null")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rank> rank = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToOne(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RecipeCategory recipeCategory;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<FoodImg> foodImgs = new ArrayList<>();

    @BatchSize(size = 100)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //이거로 넣기
    @Builder
    public Recipe(String title, String content, LocalDateTime writeDate, LocalDateTime correctionDate) {
        this.title = title;
        this.content = content;
        this.writeDate = writeDate;
        this.correctionDate = correctionDate;
    }
    //update용
    public void updateWith(Recipe updatedRecipe) {
        if (updatedRecipe.getTitle() != null) {
            this.title = updatedRecipe.getTitle();
        }

        if (updatedRecipe.getContent() != null) {
            this.content = updatedRecipe.getContent();
        }

        if (updatedRecipe.getCorrectionDate() != null) {
            this.correctionDate = updatedRecipe.getCorrectionDate();
        }
        // 원하는 엔티티의 필드에 대해 업데이트 로직을 추가
    }
    //+생성 메서드
    public Recipe createRecipe(List<RecipeDetail> recipeDetail, Member member, RecipeCategory recipeCategory) {
        Recipe recipe = new Recipe();
        recipe.setMember(member);
        for (RecipeDetail detail : recipeDetail) {
            recipe.getRecipeDetails().add(detail);
        }
        recipe.recipeCategory = recipeCategory;
        return recipe;
    }
    //조회수 증가
    public void addViewCount() {
        int addCount = 1;
        this.viewCount += addCount;
    }

    //연관관계
    public void setMember(Member member) {
        this.member = member;
        member.getRecipes().add(this);
    }
    public void setRecipeCategory(RecipeCategory recipeCategory) {
        this.recipeCategory = recipeCategory;
    }


}
