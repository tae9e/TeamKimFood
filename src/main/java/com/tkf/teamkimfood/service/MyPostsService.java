package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.domain.MyPosts;
import com.tkf.teamkimfood.repository.MyPostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyPostsService {

    @Autowired
    private MyPostsRepository myPostsRepository;

    public MyPosts getPostById(Long id) {
        return myPostsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("작성한 글이 없습니다." + id));
    }

    public void deletePost(Long id) {
        myPostsRepository.deleteById(id);
    }
}