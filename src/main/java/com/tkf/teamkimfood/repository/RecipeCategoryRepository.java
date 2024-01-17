package com.tkf.teamkimfood.repository;

import com.tkf.teamkimfood.domain.prefer.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Long> {
}
