package com.tkf.teamkimfood.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class KakaoUserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="UserInfo_id",updatable = false)
    private Long id;

    private String kakaoUserId;
    private String email;
    private String nickName;

    @OneToOne(mappedBy="kakaoUserInfo")
    private Member member;

}
