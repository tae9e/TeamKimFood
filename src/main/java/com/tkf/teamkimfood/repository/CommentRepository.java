package com.tkf.teamkimfood.repository;

import com.tkf.teamkimfood.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
