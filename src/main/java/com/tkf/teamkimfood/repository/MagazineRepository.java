package com.tkf.teamkimfood.repository;

import com.tkf.teamkimfood.domain.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {
}
