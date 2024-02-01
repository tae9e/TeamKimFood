package com.tkf.teamkimfood.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecipe is a Querydsl query type for Recipe
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecipe extends EntityPathBase<Recipe> {

    private static final long serialVersionUID = -1688475826L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecipe recipe = new QRecipe("recipe");

    public final ListPath<Comment, QComment> comments = this.<Comment, QComment>createList("comments", Comment.class, QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> correctionDate = createDateTime("correctionDate", java.time.LocalDateTime.class);

    public final ListPath<FoodImg, QFoodImg> foodImgs = this.<FoodImg, QFoodImg>createList("foodImgs", FoodImg.class, QFoodImg.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final ListPath<Rank, QRank> rank = this.<Rank, QRank>createList("rank", Rank.class, QRank.class, PathInits.DIRECT2);

    public final com.tkf.teamkimfood.domain.prefer.QRecipeCategory recipeCategory;

    public final ListPath<RecipeDetail, QRecipeDetail> recipeDetails = this.<RecipeDetail, QRecipeDetail>createList("recipeDetails", RecipeDetail.class, QRecipeDetail.class, PathInits.DIRECT2);

    public final NumberPath<Long> score = createNumber("score", Long.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> writeDate = createDateTime("writeDate", java.time.LocalDateTime.class);

    public QRecipe(String variable) {
        this(Recipe.class, forVariable(variable), INITS);
    }

    public QRecipe(Path<? extends Recipe> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecipe(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecipe(PathMetadata metadata, PathInits inits) {
        this(Recipe.class, metadata, inits);
    }

    public QRecipe(Class<? extends Recipe> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.recipeCategory = inits.isInitialized("recipeCategory") ? new com.tkf.teamkimfood.domain.prefer.QRecipeCategory(forProperty("recipeCategory"), inits.get("recipeCategory")) : null;
    }

}

