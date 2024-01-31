package com.tkf.teamkimfood.dto;

import lombok.Data;

@Data
public class LoginCredentialsVo {
    private String username;
    private String password;

    public LoginCredentialsVo(String email, String password) {
        this.username = email;
        this.password = password;
    }

    public LoginCredentialsVo() {
    }
}
