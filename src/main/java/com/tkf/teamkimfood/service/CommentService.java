package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.domain.Comment;
import com.tkf.teamkimfood.dto.CommentDto;
import com.tkf.teamkimfood.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public CommentDto saveComment(CommentDto commentDto) {
        Comment comment = toEntity(commentDto);
        Comment savedComment = commentRepository.save(comment);
        return toDto(savedComment);
    }

    @Transactional(readOnly = true)
    public CommentDto getCommentById(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        return commentOptional.map(this::toDto).orElse(null);
    }

    @Transactional
    public CommentDto updateComment(Long commentId, CommentDto updatedCommentDto) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            // 업데이트할 내용 설정
            comment.setContent(updatedCommentDto.getContent());
            // 필요한 경우 다른 필드도 업데이트
            Comment updatedComment = commentRepository.save(comment);
            return toDto(updatedComment);
        } else {
            return null;
        }
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    private Comment toEntity(CommentDto commentDto) {
        return Comment.builder()
                .id(commentDto.getId())
                .content(commentDto.getContent())
                .commentDate(commentDto.getCommentDate())
                // 다른 필드들에 대한 매핑 추가
                .build();
    }

    private CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .commentDate(comment.getCommentDate())
                // 다른 필드들에 대한 매핑 추가
                .build();
    }
}
