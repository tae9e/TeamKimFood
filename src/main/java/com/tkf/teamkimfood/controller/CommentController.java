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

@RestController
//@RequestMapping("recipes/{recipeId}/comments")
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> saveComment(@RequestBody CommentDto commentDto) {
        CommentDto savedComment = commentService.saveComment(commentDto);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("recipeId") Long commentId) {
        CommentDto commentDto = commentService.getCommentById(commentId);
        if (commentDto != null) {
            return ResponseEntity.ok(commentDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @PutMapping("/{recipeId}")
//    public ResponseEntity<CommentDto> updateComment(@PathVariable("recipeId") @AuthenticationPrincipal UserDetails userDetails) {
//        CommentDto updatedComment = commentService.updateComment(memberId, updatedCommentDto);
//        if (updatedComment != null) {
//            return ResponseEntity.ok(updatedComment);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
