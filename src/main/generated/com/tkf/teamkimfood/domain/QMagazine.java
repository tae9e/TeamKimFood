package com.tkf.teamkimfood.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMagazine is a Querydsl query type for Magazine
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMagazine extends EntityPathBase<Magazine> {

    private static final long serialVersionUID = 1600638292L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMagazine magazine = new QMagazine("magazine");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> correctionDate = createDateTime("correctionDate", java.time.LocalDateTime.class);

    public final ListPath<FoodImg, QFoodImg> foodImgs = this.<FoodImg, QFoodImg>createList("foodImgs", FoodImg.class, QFoodImg.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgName = createString("imgName");

    public final QMember member;

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> writeDate = createDateTime("writeDate", java.time.LocalDateTime.class);

    public QMagazine(String variable) {
        this(Magazine.class, forVariable(variable), INITS);
    }

    public QMagazine(Path<? extends Magazine> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMagazine(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMagazine(PathMetadata metadata, PathInits inits) {
        this(Magazine.class, metadata, inits);
    }

    public QMagazine(Class<? extends Magazine> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

