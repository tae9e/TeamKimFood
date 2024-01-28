package com.tkf.teamkimfood.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

//RefreshToken 저장, 관리
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity

public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_id",updatable = false)
    private Long id;

    @OneToOne(mappedBy = "refreshToken")
    private Member member;

    @Column(name="refresh_token",nullable = false)
    private String refreshToken;

    public RefreshToken(Member member, String refreshToken) {
        this.member = member;
        this.refreshToken = refreshToken;
    }



    public RefreshToken update(String newRefreshToken){
        this.refreshToken=newRefreshToken;
        return this;
    }
}
