package com.tkf.teamkimfood.dto;

import com.tkf.teamkimfood.domain.Comment;
import com.tkf.teamkimfood.domain.Recipe;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommentDto {

//    private Long id;
//    private String content;
//    private LocalDateTime commentDate;
//
//    @LastcorrectionDate
//    private LocalDateTime correctionDate;
//
//    private Long memberId;
//    private String nickname;
//    private Long recipeId;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public static class Request{

    private String content;

    public Comment toEntity(Recipe recipe){
        Comment comment = Comment.builder()
                .recipe(recipe)
                .content(content)
                .build();

        return comment;
    }

}

    @RequiredArgsConstructor
    @Getter
    public static class Response{
        private Long id;
        private String content;
        private String nickname;
        private Long memberId;
        private List<Response> comments;
        private LocalDateTime commentDate;
        private LocalDateTime correctionDate;
        private Boolean isWriter;

        public Response(Comment comment, Long currentMemberId, Long recipeWriterId){
            this.id = comment.getId();
            this.nickname = comment.getMember().getNickname();
            this.memberId = comment.getMember().getId();
            if(comment.getComments()!= null){
                this.comments = comment.getComments().stream().map(c-> new CommentDto.Response(c, currentMemberId, recipeWriterId)).collect(Collectors.toList());
            }
            this.commentDate = comment.getCommentDate();
            this.correctionDate = comment.getCorrectionDate();

            if(Objects.equals(currentMemberId, comment.getMember().getId())) this.isWriter = true;
            else this.isWriter = false;

        }
    }
}
