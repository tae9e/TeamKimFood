package com.tkf.teamkimfood.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1831330726L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final ListPath<Comment, QComment> comments = this.<Comment, QComment>createList("comments", Comment.class, QComment.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final ListPath<FoodFile, QFoodFile> foodFiles = this.<FoodFile, QFoodFile>createList("foodFiles", FoodFile.class, QFoodFile.class, PathInits.DIRECT2);

    public final ListPath<FoodImg, QFoodImg> foodImgs = this.<FoodImg, QFoodImg>createList("foodImgs", FoodImg.class, QFoodImg.class, PathInits.DIRECT2);

    public final NumberPath<Integer> grade = createNumber("grade", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> joinedDate = createDateTime("joinedDate", java.time.LocalDateTime.class);

    public final QKakaoUserInfo kakaoUserInfo;

    public final ListPath<Magazine, QMagazine> magazines = this.<Magazine, QMagazine>createList("magazines", Magazine.class, QMagazine.class, PathInits.DIRECT2);

    public final com.tkf.teamkimfood.domain.prefer.QMemberPreference memberPreference;

    public final BooleanPath memberRecommend = createBoolean("memberRecommend");

    public final EnumPath<com.tkf.teamkimfood.domain.status.MemberRole> memberRole = createEnum("memberRole", com.tkf.teamkimfood.domain.status.MemberRole.class);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final EnumPath<com.tkf.teamkimfood.config.oauth.OAuthProvider> oAuthProvider = createEnum("oAuthProvider", com.tkf.teamkimfood.config.oauth.OAuthProvider.class);

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final ListPath<Rank, QRank> rank = this.<Rank, QRank>createList("rank", Rank.class, QRank.class, PathInits.DIRECT2);

    public final BooleanPath recipeRecommend = createBoolean("recipeRecommend");

    public final ListPath<Recipe, QRecipe> recipes = this.<Recipe, QRecipe>createList("recipes", Recipe.class, QRecipe.class, PathInits.DIRECT2);

    public final QRefreshToken refreshToken;

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.kakaoUserInfo = inits.isInitialized("kakaoUserInfo") ? new QKakaoUserInfo(forProperty("kakaoUserInfo"), inits.get("kakaoUserInfo")) : null;
        this.memberPreference = inits.isInitialized("memberPreference") ? new com.tkf.teamkimfood.domain.prefer.QMemberPreference(forProperty("memberPreference"), inits.get("memberPreference")) : null;
        this.refreshToken = inits.isInitialized("refreshToken") ? new QRefreshToken(forProperty("refreshToken"), inits.get("refreshToken")) : null;
    }

}

