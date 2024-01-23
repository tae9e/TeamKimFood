package com.tkf.teamkimfood.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;
    private String content;
    private LocalDateTime commentDate;
    private LocalDateTime correctionDate;

    public CommentDto() {
    }

    public CommentDto(Long id, String content, LocalDateTime commentDate, LocalDateTime correctionDate) {
        this.id = id;
        this.content = content;
        this.commentDate = commentDate;
        this.correctionDate = correctionDate;
    }
}
