package com.tkf.teamkimfood.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFoodFile is a Querydsl query type for FoodFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFoodFile extends EntityPathBase<FoodFile> {

    private static final long serialVersionUID = 2056272058L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFoodFile foodFile = new QFoodFile("foodFile");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath fileName = createString("fileName");

    public final StringPath fileType = createString("fileType");

    public final ListPath<FoodImg, QFoodImg> foodImgs = this.<FoodImg, QFoodImg>createList("foodImgs", FoodImg.class, QFoodImg.class, PathInits.DIRECT2);

    public final NumberPath<Long> Id = createNumber("Id", Long.class);

    public final QMember member;

    public QFoodFile(String variable) {
        this(FoodFile.class, forVariable(variable), INITS);
    }

    public QFoodFile(Path<? extends FoodFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFoodFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFoodFile(PathMetadata metadata, PathInits inits) {
        this(FoodFile.class, metadata, inits);
    }

    public QFoodFile(Class<? extends FoodFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

