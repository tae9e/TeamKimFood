package com.tkf.teamkimfood.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMyPosts is a Querydsl query type for MyPosts
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyPosts extends EntityPathBase<MyPosts> {

    private static final long serialVersionUID = -390475833L;

    public static final QMyPosts myPosts = new QMyPosts("myPosts");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> recipeId = createNumber("recipeId", Long.class);

    public final StringPath title = createString("title");

    public QMyPosts(String variable) {
        super(MyPosts.class, forVariable(variable));
    }

    public QMyPosts(Path<? extends MyPosts> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMyPosts(PathMetadata metadata) {
        super(MyPosts.class, metadata);
    }

}

