package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.domain.Comment;
import com.tkf.teamkimfood.dto.CommentDto;
import com.tkf.teamkimfood.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void saveComment() {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent("Test comment");
        commentDto.setCommentDate(LocalDateTime.now());

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent(commentDto.getContent());
        comment.setCommentDate(commentDto.getCommentDate());

        when(commentRepository.save(any())).thenReturn(comment);

        CommentDto savedCommentDto = commentService.saveComment(commentDto);

        assertEquals(comment.getId(), savedCommentDto.getId());
        assertEquals(comment.getContent(), savedCommentDto.getContent());
        assertEquals(comment.getCommentDate(), savedCommentDto.getCommentDate());

        verify(commentRepository, times(1)).save(any());
    }

    @Test
    void getCommentById() {
        Long commentId = 1L;

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setContent("Test comment");
        comment.setCommentDate(LocalDateTime.now());

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        CommentDto commentDto = commentService.getCommentById(commentId);

        assertEquals(comment.getId(), commentDto.getId());
        assertEquals(comment.getContent(), commentDto.getContent());
        assertEquals(comment.getCommentDate(), commentDto.getCommentDate());

        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    public void testDeleteComment() {
        Long commentId = 1L;

        commentService.deleteComment(commentId);

        verify(commentRepository, times(1)).deleteById(commentId);
    }
}