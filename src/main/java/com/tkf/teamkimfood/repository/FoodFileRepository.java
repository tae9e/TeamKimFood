package com.tkf.teamkimfood.repository;

import com.tkf.teamkimfood.domain.FoodFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodFileRepository extends JpaRepository<FoodFile, Long> {
}
