package com.tkf.teamkimfood.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MemberScoreTotalDto {

    private Long id;
    private String nickname;
    private Long totalScore;

    @QueryProjection
    public MemberScoreTotalDto(Long id, String nickname, Long totalScore) {
        this.id = id;
        this.nickname = nickname;
        this.totalScore = totalScore;
    }
}
