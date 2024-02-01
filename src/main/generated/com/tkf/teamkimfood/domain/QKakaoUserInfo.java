package com.tkf.teamkimfood.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QKakaoUserInfo is a Querydsl query type for KakaoUserInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKakaoUserInfo extends EntityPathBase<KakaoUserInfo> {

    private static final long serialVersionUID = -1833674948L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QKakaoUserInfo kakaoUserInfo = new QKakaoUserInfo("kakaoUserInfo");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath kakaoUserId = createString("kakaoUserId");

    public final QMember member;

    public final StringPath nickName = createString("nickName");

    public QKakaoUserInfo(String variable) {
        this(KakaoUserInfo.class, forVariable(variable), INITS);
    }

    public QKakaoUserInfo(Path<? extends KakaoUserInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QKakaoUserInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QKakaoUserInfo(PathMetadata metadata, PathInits inits) {
        this(KakaoUserInfo.class, metadata, inits);
    }

    public QKakaoUserInfo(Class<? extends KakaoUserInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

