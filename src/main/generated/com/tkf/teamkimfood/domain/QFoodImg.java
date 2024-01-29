package com.tkf.teamkimfood.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFoodImg is a Querydsl query type for FoodImg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFoodImg extends EntityPathBase<FoodImg> {

    private static final long serialVersionUID = 1728902341L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFoodImg foodImg = new QFoodImg("foodImg");

    public final StringPath explanation = createString("explanation");

    public final QFoodFile foodFile;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgName = createString("imgName");

    public final StringPath imgUrl = createString("imgUrl");

    public final QMagazine magazine;

    public final QMember member;

    public final StringPath originImgName = createString("originImgName");

    public final QRecipe recipe;

    public final StringPath repImgYn = createString("repImgYn");

    public QFoodImg(String variable) {
        this(FoodImg.class, forVariable(variable), INITS);
    }

    public QFoodImg(Path<? extends FoodImg> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFoodImg(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFoodImg(PathMetadata metadata, PathInits inits) {
        this(FoodImg.class, metadata, inits);
    }

    public QFoodImg(Class<? extends FoodImg> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.foodFile = inits.isInitialized("foodFile") ? new QFoodFile(forProperty("foodFile"), inits.get("foodFile")) : null;
        this.magazine = inits.isInitialized("magazine") ? new QMagazine(forProperty("magazine"), inits.get("magazine")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.recipe = inits.isInitialized("recipe") ? new QRecipe(forProperty("recipe"), inits.get("recipe")) : null;
    }

}

