package com.tkf.teamkimfood.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tkf.teamkimfood.dto.CommentDto;
import com.tkf.teamkimfood.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();

        // ObjectMapper에 LocalDateTime 직렬화 모듈 등록
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @WithMockUser
    public void testSaveComment() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .content("Test comment")
                .commentDate(LocalDateTime.now())
                .build();

        CommentDto savedCommentDto = CommentDto.builder()
                .id(1L)
                .content(commentDto.getContent())
                .commentDate(commentDto.getCommentDate())
                .build();

        when(commentService.saveComment(any(CommentDto.class))).thenReturn(savedCommentDto);

        String requestJson = objectMapper.writeValueAsString(commentDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());

        verify(commentService, times(1)).saveComment(any(CommentDto.class));
    }

    @Test
    @WithMockUser
    public void testGetCommentById() throws Exception {
        Long commentId = 1L;

        CommentDto commentDto = CommentDto.builder()
                .id(commentId)
                .content("Test comment")
                .commentDate(LocalDateTime.now())
                .build();

        when(commentService.getCommentById(commentId)).thenReturn(commentDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/comments/{id}", commentId))
                .andExpect(status().isOk());

        verify(commentService, times(1)).getCommentById(commentId);
    }

    @Test
    @WithMockUser
    public void testDeleteComment() throws Exception {
        Long commentId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/{id}", commentId))
                .andExpect(status().isNoContent());

        verify(commentService, times(1)).deleteComment(commentId);
    }
}