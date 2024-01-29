package com.tkf.teamkimfood.controller;

import com.tkf.teamkimfood.domain.MyPosts;
import com.tkf.teamkimfood.repository.MyPostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MyPostsController {

    @Autowired
    private MyPostsRepository myPostsRepository;

    @GetMapping("/mypage/mypost")
    public List<MyPosts> getMyPosts() {
        return myPostsRepository.findAll();
    }
}
