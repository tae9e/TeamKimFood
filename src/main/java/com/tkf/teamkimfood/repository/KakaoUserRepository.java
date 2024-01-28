package com.tkf.teamkimfood.repository;

import com.tkf.teamkimfood.domain.KakaoUserInfo;
import com.tkf.teamkimfood.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoUserRepository extends JpaRepository<KakaoUserInfo, Long> {
    Optional<KakaoUserInfo> findByEmail(String email);
}
