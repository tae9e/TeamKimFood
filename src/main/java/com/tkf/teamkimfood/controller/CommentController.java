package com.tkf.teamkimfood.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkf.teamkimfood.dto.CommentDto;
import com.tkf.teamkimfood.dto.aboutrecipe.RecipeRequestVo;
import com.tkf.teamkimfood.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
//@RequestMapping("http://localhost:8080/api/comments")
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

//    @PostMapping
//    public ResponseEntity<CommentDto> saveComment(@RequestBody CommentDto commentDto, @AuthenticationPrincipal UserDetails userDetails) {
//        // UserDetails에서 username(email)을 추출
//        String email = userDetails.getUsername();
//        CommentDto savedComment = commentService.saveComment(commentDto, email);
//        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
//    }

    @PostMapping
    public ResponseEntity<CommentDto> saveComment(@RequestBody CommentDto commentDto) {
        CommentDto savedComment = commentService.saveComment(commentDto);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    // 레시피 ID에 따른 댓글 불러오기 기능으로 변경
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<CommentDto>> getCommentsByRecipeId(@PathVariable("recipeId") Long recipeId) {
        List<CommentDto> comments = commentService.getCommentsByRecipeId(recipeId);
        if(comments != null) {
            return ResponseEntity.ok(comments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //    @PutMapping("/recipe/{recipeId}")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("recipeId") Long commentId, @RequestBody CommentDto commentDto) {
        CommentDto updatedComment = commentService.updateComment(commentId, commentDto);
        if (updatedComment != null) {
            return ResponseEntity.ok(updatedComment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //    @DeleteMapping("/recipe/{recipeId}")
    @DeleteMapping
    public ResponseEntity<Void> deleteComment(@PathVariable("recipeId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
