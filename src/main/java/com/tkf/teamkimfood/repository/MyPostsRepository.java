package com.tkf.teamkimfood.repository;

import com.tkf.teamkimfood.domain.MyPosts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyPostsRepository extends JpaRepository<MyPosts, Long> {

}
