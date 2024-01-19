package com.tkf.teamkimfood.repository.query;

import com.tkf.teamkimfood.domain.RefreshToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRespository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserId(Long userId);

    Page<RefreshToken> findByRefreshToken(String refreshToken,Pageable pageable);
}
