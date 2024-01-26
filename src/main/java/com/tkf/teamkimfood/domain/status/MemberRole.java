package com.tkf.teamkimfood.domain.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
//    USER, ADMIN
    USER("USER"),
    ADMIN("ADMIN");

    private final String value;
}
