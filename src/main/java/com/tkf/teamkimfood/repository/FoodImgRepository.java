package com.tkf.teamkimfood.repository;

import com.tkf.teamkimfood.domain.FoodImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodImgRepository extends JpaRepository<FoodImg, Long> {

    @Query("select fi from FoodImg fi where fi.recipe.id = :id order by fi.id asc ")
    List<FoodImg> findByRecipeIdOrderByIdAsc(@Param("id") Long Id);

    //레시피의 대표 이미지 찾기
    FoodImg findByRecipeIdAndRepImgYn(Long recipeId, String repImgYn);

}
