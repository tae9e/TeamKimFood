package com.tkf.teamkimfood.dbtest;

import lombok.Data;

@Data
public class TestDto {

    private String email;
    private String name;

    public TestDto(TestEntity testEntity) {
        this.email = testEntity.getEmail();
        this.name = testEntity.getName();
    }
}
