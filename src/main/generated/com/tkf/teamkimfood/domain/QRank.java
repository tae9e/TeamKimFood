package com.tkf.teamkimfood.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRank is a Querydsl query type for Rank
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRank extends EntityPathBase<Rank> {

    private static final long serialVersionUID = 1258573292L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRank rank = new QRank("rank");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final NumberPath<Long> memberRecoTotal = createNumber("memberRecoTotal", Long.class);

    public final EnumPath<com.tkf.teamkimfood.domain.status.RankSearchStatus> rankSearchStatus = createEnum("rankSearchStatus", com.tkf.teamkimfood.domain.status.RankSearchStatus.class);

    public final QRecipe recipe;

    public final BooleanPath recipeRecommendation = createBoolean("recipeRecommendation");

    public final NumberPath<Long> recipeRecoTotal = createNumber("recipeRecoTotal", Long.class);

    public final BooleanPath userRecommendation = createBoolean("userRecommendation");

    public QRank(String variable) {
        this(Rank.class, forVariable(variable), INITS);
    }

    public QRank(Path<? extends Rank> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRank(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRank(PathMetadata metadata, PathInits inits) {
        this(Rank.class, metadata, inits);
    }

    public QRank(Class<? extends Rank> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.recipe = inits.isInitialized("recipe") ? new QRecipe(forProperty("recipe"), inits.get("recipe")) : null;
    }

}

