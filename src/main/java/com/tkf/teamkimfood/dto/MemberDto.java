package com.tkf.teamkimfood.dto;

import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.status.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {
    private String name;
    private String password;
    private String email;
    private String nickName;
    private String phoneNumber;
    private MemberRole memberRole;

    public Member toEntity() {
        Member member = Member.builder()
                .name(name)
                .password(password)
                .nickname(nickName)
                .email(email)
                .memberRole(memberRole.USER)
                .build();
        return member;
    }
}
