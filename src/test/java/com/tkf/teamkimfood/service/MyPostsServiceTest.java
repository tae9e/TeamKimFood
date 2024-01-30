package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.domain.MyPosts;
import com.tkf.teamkimfood.repository.MyPostsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyPostsServiceTest {

    @Mock
    private MyPostsRepository myPostsRepository;

    @InjectMocks
    private MyPostsService myPostsService;

    @Test
    public void testGetPostById() {

        MyPosts mockPost = new MyPosts();
        mockPost.setId(1L);
        mockPost.setTitle("테스트 제목");
        when(myPostsRepository.findById(1L)).thenReturn(Optional.of(mockPost));

        MyPosts retrievedPost = myPostsService.getPostById(1L);

        assertEquals("테스트 제목", retrievedPost.getTitle());
    }

    @Test
    public void testDeletePost() {

        Long postId = 1L;

        myPostsService.deletePost(postId);

        verify(myPostsRepository, times(1)).deleteById(postId);
    }
}