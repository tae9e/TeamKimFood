package com.tkf.teamkimfood.domain.prefer;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberPreference is a Querydsl query type for MemberPreference
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberPreference extends EntityPathBase<MemberPreference> {

    private static final long serialVersionUID = 1992905139L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberPreference memberPreference = new QMemberPreference("memberPreference");

    public final StringPath foodNationType = createString("foodNationType");

    public final StringPath foodStuff = createString("foodStuff");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.tkf.teamkimfood.domain.QMember member;

    public final StringPath Situation = createString("Situation");

    public QMemberPreference(String variable) {
        this(MemberPreference.class, forVariable(variable), INITS);
    }

    public QMemberPreference(Path<? extends MemberPreference> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberPreference(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberPreference(PathMetadata metadata, PathInits inits) {
        this(MemberPreference.class, metadata, inits);
    }

    public QMemberPreference(Class<? extends MemberPreference> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.tkf.teamkimfood.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

