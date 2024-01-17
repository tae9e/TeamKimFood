package com.tkf.teamkimfood.repository;

import com.tkf.teamkimfood.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    public List<Recipe> findAllByOrderByWriteDateDesc();

}
