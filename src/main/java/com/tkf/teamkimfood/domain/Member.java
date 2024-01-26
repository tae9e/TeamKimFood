package com.tkf.teamkimfood.domain;

import com.tkf.teamkimfood.config.oauth.OAuthProvider;
import com.tkf.teamkimfood.domain.prefer.MemberPreference;
import com.tkf.teamkimfood.domain.status.MemberRole;
import com.tkf.teamkimfood.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member {


    @Id @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "member")
    private RefreshToken refreshToken;

    private String name;
    private String password;

    private String email;

    private String nickname;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    //Oauth 로그인을 위한 필드
    @Column(name = "kakao_user_id")
    private String kakaoUserId;


    @Column(name = "kakao_access_token")
    private String kakaoAccessToken;

    @ColumnDefault("0")
    private int grade;

    private boolean memberRecommend = false;
    private boolean recipeRecommend = false;
    //멤버 레시피 갯수->01.15 : 따로 저장 할 필요 없이 findAll이후 List.size()해버리면 된다.
//    @ColumnDefault("0")
//    private int stack;

    private LocalDateTime joinedDate;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Magazine> magazines = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rank> rank = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MemberPreference memberPreference;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<FoodFile> foodFiles = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Recipe> recipes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<FoodImg> foodImgs = new ArrayList<>();


    //Oauth
    private OAuthProvider oAuthProvider;


    //여기를 통해 데이터 넣으세용 id는 멤버가 아직 만들어지기 전이라 임시로 넣었습니다. 추후 프론트랑 연결시 삭제하겠습니다.
    @Builder
    public Member(Long id, String name, String password, String email, String nickname, String phoneNumber, MemberRole memberRole, LocalDateTime joinedDate,
                  MemberPreference memberPreference, OAuthProvider oAuthProvider,String kakaoUserId,String kakaoAccessToken) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.memberRole = memberRole;
        this.joinedDate = joinedDate;
        this.memberPreference = memberPreference;
        this.oAuthProvider = oAuthProvider;
        this.kakaoUserId=kakaoUserId;
        this.kakaoAccessToken=kakaoAccessToken;
    }
    //테스트용 테스트가 끝나면 주석처리하시거나 삭제 해주세요

    // 레시피 작성 횟수 관련 - 다른곳으로 뺄 예정

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setEmail(memberFormDto.getEmail());
        member.setNickname(memberFormDto.getNickname());
        member.setPhoneNumber(memberFormDto.getPhoneNumber());
        //member.setMemberRole(MemberRole.USER);
        member.setMemberRole(MemberRole.ADMIN);
        return member;
    }



}
