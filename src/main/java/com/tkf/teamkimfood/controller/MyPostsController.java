package com.tkf.teamkimfood.controller;

import com.tkf.teamkimfood.domain.MyPosts;
import com.tkf.teamkimfood.service.MyPostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyPostsController {

    @Autowired
    private MyPostsService myPostsService;

    @GetMapping("/mypage/myposts/{id}")
    public MyPosts getPostById(@PathVariable Long id) {
        return myPostsService.getPostById(id);
    }

    @DeleteMapping("/mypage/myposts/{id}")
    public void deletePost(@PathVariable Long id) {
        myPostsService.deletePost(id);
    }
}
