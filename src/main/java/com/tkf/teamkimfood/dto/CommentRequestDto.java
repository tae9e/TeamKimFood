package com.tkf.teamkimfood.dto;

import com.tkf.teamkimfood.domain.Comment;
import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {

    private Long id;
    private String content;
    private LocalDateTime commentDate;
    private LocalDateTime correctionDate;
    private Member memberId;
    private Recipe recipeId;

    public Comment toEntity() {
        Comment comments = Comment.builder()
                .id(id)
                .content(content)
                .commentDate(commentDate)
                .correctionDate(correctionDate)
                .member(memberId)
                .recipe(recipeId)
                .build();
        return comments;
    }
}
