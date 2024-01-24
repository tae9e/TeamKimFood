package com.tkf.teamkimfood.repository;

import com.tkf.teamkimfood.domain.RefreshToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//RefreshToken과 관련된 데이터베이스 작업 수행
public interface RefreshTokenRespository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByMember_Id(Long memberId);

    Page<RefreshToken> findByRefreshToken(String refreshToken,Pageable pageable);
}
