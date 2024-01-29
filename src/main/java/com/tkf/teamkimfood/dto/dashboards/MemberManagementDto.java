package com.tkf.teamkimfood.dto.dashboards;

import lombok.Data;

@Data
public class MemberManagementDto {

    private Long id;
    private String name;
    private String email;
    private String nickName;
    private String phoneNumber;


    public MemberManagementDto(Long id, String name, String email, String nickName, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
    }
}
