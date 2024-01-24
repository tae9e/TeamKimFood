package com.tkf.teamkimfood.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class FileService {
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws IOException {
        //범용 공용 식별자, 랜덤으로 붙는거 쓰겠다.
        UUID uuid = UUID.randomUUID();
        log.info("originalFileName : "+originalFileName);
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString()+extension;
        String fileUploadFullUrl = uploadPath+"/"+savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();

        return savedFileName;//업로드된 파일
    }

    public void deleteFile(String filePath){
        File deleteFile = new File(filePath);
        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        }else{
            log.info("파일을 삭제하지 못했습니다. 파일이 존재하지 않습니다.");
        }
    }
}
