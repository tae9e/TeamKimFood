package com.tkf.teamkimfood.domain.prefer;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecipeCategory is a Querydsl query type for RecipeCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecipeCategory extends EntityPathBase<RecipeCategory> {

    private static final long serialVersionUID = -831373622L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecipeCategory recipeCategory = new QRecipeCategory("recipeCategory");

    public final StringPath foodNationType = createString("foodNationType");

    public final StringPath foodStuff = createString("foodStuff");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.tkf.teamkimfood.domain.QMember member;

    public final com.tkf.teamkimfood.domain.QRecipe recipe;

    public final StringPath Situation = createString("Situation");

    public QRecipeCategory(String variable) {
        this(RecipeCategory.class, forVariable(variable), INITS);
    }

    public QRecipeCategory(Path<? extends RecipeCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecipeCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecipeCategory(PathMetadata metadata, PathInits inits) {
        this(RecipeCategory.class, metadata, inits);
    }

    public QRecipeCategory(Class<? extends RecipeCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.tkf.teamkimfood.domain.QMember(forProperty("member"), inits.get("member")) : null;
        this.recipe = inits.isInitialized("recipe") ? new com.tkf.teamkimfood.domain.QRecipe(forProperty("recipe"), inits.get("recipe")) : null;
    }

}

