package com.shop.service;

import com.shop.entity.OqnaImg;
import com.shop.repository.OqnaImgRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class OqnaImgService {

	  @Value("${oqnaImgLocation}")
	    private String oqnaImgLocation;

	    private final OqnaImgRepository oqnaImgRepository;

	    private final FileService fileService;

	    public void saveItemImg(OqnaImg oqnaImg, MultipartFile oqnaImgFile) throws Exception{
	        String oriImgName = oqnaImgFile.getOriginalFilename();
	        String imgName = "";
	        String imgUrl = "";

	        //파일 업로드
	        if(!StringUtils.isEmpty(oriImgName)){
	            imgName = fileService.uploadFile(oqnaImgLocation, oriImgName,
	                    oqnaImgFile.getBytes());
	            imgUrl = "/images/item/" + imgName;
	        }

	        //상품 이미지 정보 저장
	        oqnaImg.updateItemImg(oriImgName, imgName, imgUrl);
	        oqnaImgRepository.save(oqnaImg);
	    }

	    public void updateOqnaImg(Long oqnaImgId, MultipartFile oqnaImgFile) throws Exception{
	        if(!oqnaImgFile.isEmpty()){
	            OqnaImg savedOqnaImg = oqnaImgRepository.findById(oqnaImgId)
	                    .orElseThrow(EntityNotFoundException::new);

	            //기존 이미지 파일 삭제
	            if(!StringUtils.isEmpty(savedOqnaImg.getImgName())) {
	                fileService.deleteFile(oqnaImgLocation+"/"+
	                        savedOqnaImg.getImgName());
	            }

	            String oriImgName = oqnaImgFile.getOriginalFilename();
	            String imgName = fileService.uploadFile(oqnaImgLocation, oriImgName, oqnaImgFile.getBytes());
	            String imgUrl = "/images/item/" + imgName;
	            savedOqnaImg.updateItemImg(oriImgName, imgName, imgUrl);
	        }
	    }

}