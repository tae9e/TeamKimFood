package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.domain.Comment;
import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.Recipe;
import com.tkf.teamkimfood.dto.CommentDto;
import com.tkf.teamkimfood.repository.CommentRepository;
import com.tkf.teamkimfood.repository.MemberRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final RecipeRepository recipeRepository;

//    @Transactional
//    public CommentDto saveComment(CommentDto commentDto, String email) {
//    Member member = memberRepository.findByEmail(email)
//            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));
//    Recipe recipe = recipeRepository.findById(commentDto.getRecipeId())
//            .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));
//
//    Comment comment = Comment.builder()
//            .content(commentDto.getContent())
//            .commentDate(commentDto.getCommentDate())
//            .member(member)
//            .recipe(recipe)
//            .build();
//
//    Comment savedComment = commentRepository.save(comment);
//    return toDto(savedComment);
//}

    @Transactional
    public CommentDto saveComment(CommentDto commentDto) {
        Member member = memberRepository.findById(commentDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 멤버 ID입니다."));
        Recipe recipe = recipeRepository.findById(commentDto.getRecipeId())
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .commentDate(commentDto.getCommentDate())
                .member(member)
                .recipe(recipe)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return toDto(savedComment);
    }

    @Transactional
    public CommentDto updateComment(Long commentId, CommentDto updatedCommentDto) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setContent(updatedCommentDto.getContent());
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

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByRecipeId(Long recipeId) {
        List<Comment> comments = commentRepository.findByRecipeId(recipeId);
        return comments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .commentDate(comment.getCommentDate())
                .memberId(comment.getMember().getId()) // memberId 추가
                .recipeId(comment.getRecipe().getId()) // recipeId 추가
                .build();
    }
}