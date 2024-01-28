package com.tkf.teamkimfood.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberFormDto {
    private Long memberId;
    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;

    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    @Size(min=8,message="비밀번호는 최소 8자리 이상이어야 합니다.")
    private String password;

    //이메일 형식 @ 필수, 하나 이상의 .
    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotEmpty(message = "닉네임을 설정해주세요")
    private String nickname;

    //010 (- or .) 3 or 4자리 (- or .) - 4자리
    @NotEmpty(message = "전화번호를 입력해주세요")
    @Pattern(regexp = "^010[.-]?(\\d{3}|\\d{4})[-]?(\\d{4})$",message = "전화번호는 000-0000-0000으로 작성해주세요. ")
    private String phoneNumber;



}
