package com.tkf.teamkimfood.repository;

import com.tkf.teamkimfood.domain.FoodImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodImgRepository extends JpaRepository<FoodImg, Long> {

    List<FoodImg> findByRecipeIdOrderByIdAsc(Long itemId);

    //레시피의 대표 이미지 찾기
    FoodImg findByRecipeIdAndRepImgYn(Long recipeId, String repImgYn);

}
