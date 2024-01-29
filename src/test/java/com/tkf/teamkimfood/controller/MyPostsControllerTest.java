package com.tkf.teamkimfood.controller;

import com.tkf.teamkimfood.domain.MyPosts;
import com.tkf.teamkimfood.service.MyPostsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
public class MyPostsControllerTest {

    @Mock
    private MyPostsService myPostsService;

    @InjectMocks
    private MyPostsController myPostsController;

    @Test
    public void testGetPostById() throws Exception {

        Long postId = 1L;
        MyPosts mockPost = new MyPosts();
        mockPost.setId(postId);
        mockPost.setTitle("테스트 제목");
        when(myPostsService.getPostById(postId)).thenReturn(mockPost);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(myPostsController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/mypage/myposts/{id}", postId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(postId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("테스트 제목"))
                .andDo(print());
    }

    @Test
    public void testDeletePost() throws Exception {

        Long postId = 1L;

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(myPostsController).build();
        mockMvc.perform(MockMvcRequestBuilders.delete("/mypage/myposts/{id}", postId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        verify(myPostsService, times(1)).deletePost(postId);
    }
}
