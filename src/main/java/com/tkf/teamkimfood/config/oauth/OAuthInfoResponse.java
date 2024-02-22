package com.tkf.teamkimfood.config.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//사용자 정보 저장
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OAuthInfoResponse {
 private String email;
 private String nickName;
 private OAuthProvider oAuthProvider;
}
