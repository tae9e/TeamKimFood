package com.tkf.teamkimfood.repository;

import com.tkf.teamkimfood.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMemberEmail(String email);

}
