package com.tkf.teamkimfood.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

    private Long id;
    private String content;
    private LocalDateTime commentDate;
    private LocalDateTime correctionDate;
    private Long memberId;
    private String nickname;
    private Long recipeId;
    private List<CommentDto> comments;

}