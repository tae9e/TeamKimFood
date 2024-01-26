package com.tkf.teamkimfood.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    private String name;
    private String password;
    private String email;
    private String nickName;
    private String phoneNumber;
}
