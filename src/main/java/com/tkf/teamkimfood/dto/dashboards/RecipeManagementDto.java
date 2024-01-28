package com.tkf.teamkimfood.dto.dashboards;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecipeManagementDto {

    private Long id;
    private String title;
    private String nickname;
    private int viewCount;
    private LocalDateTime writeDate;

    public RecipeManagementDto(Long id, String title, String nickname, int viewCount, LocalDateTime writeDate) {
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.viewCount = viewCount;
        this.writeDate = writeDate;
    }
}
