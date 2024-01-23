package com.tkf.teamkimfood.service;

import com.tkf.teamkimfood.domain.FoodImg;
import com.tkf.teamkimfood.repository.FoodImgRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodImgService {
    @Value("${recipeImgLocation}")
    private String recipeImgLocation;

    private final FileService fileService;
    private final FoodImgRepository foodImgRepository;

    public void saveFoodImg(FoodImg foodImg, String explanation, MultipartFile foodImgFile) throws IOException {
        String originImgName = foodImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";
        //파일 업로드
        if (originImgName != null) {
            imgName = fileService.uploadFile(recipeImgLocation, originImgName, foodImgFile.getBytes());
            imgUrl = "/images/recipe/"+imgName;
        }
        //입력받은 상품 이미지 정보 저장
        foodImg.updateExplain(explanation);
        foodImg.updateItemImg(imgName, originImgName,imgUrl);
        foodImgRepository.save(foodImg);
    }

    public void updateFoodImg(Long foodImgId, String explanation ,MultipartFile foodImgFile) throws IOException {
        if (!foodImgFile.isEmpty()) {
            FoodImg savedFoodImg = foodImgRepository.findById(foodImgId)
                    .orElseThrow(EntityExistsException::new);
            if (!savedFoodImg.getExplanation().equals(explanation)) {
                savedFoodImg.updateExplain(explanation);
            }
            //기존이미지파일 삭제
            if (!savedFoodImg.getImgName().isEmpty()){
                fileService.deleteFile(recipeImgLocation+"/"+savedFoodImg.getImgName());
            }
            String originImgName = foodImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(recipeImgLocation, originImgName, foodImgFile.getBytes());
            String imgUrl = "/image/recipe/"+imgName;
            savedFoodImg.updateItemImg(imgName,originImgName,imgUrl);
        }
    }
}
