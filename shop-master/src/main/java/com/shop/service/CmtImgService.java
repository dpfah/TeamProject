package com.shop.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.shop.entity.CmtImg;
import com.shop.repository.CmtImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CmtImgService {
	// application.properties 파일에 등록한 cmtimgLocation값을 불러와서 cmtImgLocation 변수에 넣어줌
	 @Value("${cmtImgLocation}")
	    private String cmtImgLocation;

	    private final CmtImgRepository cmtImgRepository;

	    private final FileService fileService;

	    public void saveCmtImg(CmtImg cmtImg, MultipartFile cmtImgFile) throws Exception{
	        String oriImgName = cmtImgFile.getOriginalFilename();
	        String imgName = "";
	        String imgUrl = "";

	        //파일 업로드
	        if(!StringUtils.isEmpty(oriImgName)){
	            imgName = fileService.uploadFile(cmtImgLocation, oriImgName,
	                    cmtImgFile.getBytes());
	            imgUrl = "/images/cmt/" + imgName; //c드라이브 안에 shop 폴더안에 cmt폴더를 만들어줘야함. 그리고 application.properties에 경로 설정을 해준다.
	        }

	        //이미지 정보 저장
	        cmtImg.updateCmtImg(oriImgName, imgName, imgUrl);
	        cmtImgRepository.save(cmtImg);
	    }

	    public void updateCmtImg(Long cmtImgId, MultipartFile cmtImgFile) throws Exception{
	        if(!cmtImgFile.isEmpty()){
	            CmtImg savedCmtImg = cmtImgRepository.findById(cmtImgId)
	                    .orElseThrow(EntityNotFoundException::new);

	            //기존 이미지 파일 삭제
	            if(!StringUtils.isEmpty(savedCmtImg.getImgName())) {
	                fileService.deleteFile(cmtImgLocation+"/"+
	                        savedCmtImg.getImgName());
	            }

	            String oriImgName = cmtImgFile.getOriginalFilename();
	            String imgName = fileService.uploadFile(cmtImgLocation, oriImgName, cmtImgFile.getBytes());
	            String imgUrl = "/images/cmt/" + imgName;
	            savedCmtImg.updateCmtImg(oriImgName, imgName, imgUrl);
	        }
	    }

  

}