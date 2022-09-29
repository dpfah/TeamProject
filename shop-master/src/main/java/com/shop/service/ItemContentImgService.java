package com.shop.service;

import javax.persistence.EntityNotFoundException;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.shop.entity.ItemContentImg;
import com.shop.repository.ItemContentImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemContentImgService {
	// application.properties 파일에 등록한 itemContentimgLocation값을 불러와서 itemContentImgLocation 변수에 넣어줌
	 @Value("${itemContentImgLocation}")
	    private String itemContentImgLocation;

	    private final ItemContentImgRepository itemContentImgRepository;

	    private final FileService fileService;

	    public void saveItemContentImg(ItemContentImg itemContentImg, MultipartFile itemContentImgFile) throws Exception{
	        String oriImgName = itemContentImgFile.getOriginalFilename();
	        String imgName = "";
	        String imgUrl = "";

	        //파일 업로드
	        if(!StringUtils.isEmpty(oriImgName)){
	            imgName = fileService.uploadFile(itemContentImgLocation, oriImgName,
	                    itemContentImgFile.getBytes());
	            imgUrl = "/images/item/content/" + imgName;
	        }

	        //상품 이미지 정보 저장
	        itemContentImg.updateItemContentImg(oriImgName, imgName, imgUrl);
	        itemContentImgRepository.save(itemContentImg);
	    }

	    public void updateItemContentImg(Long itemContentImgId, MultipartFile itemContentImgFile) throws Exception{
			if(!itemContentImgFile.isEmpty()){
	            ItemContentImg savedItemContentImg = itemContentImgRepository.findById(itemContentImgId)
	                    .orElseThrow(EntityNotFoundException::new);

	            //기존 이미지 파일 삭제
	            if(!StringUtils.isEmpty(savedItemContentImg.getImgName())) {
	                fileService.deleteFile(itemContentImgLocation+"/"+
	                        savedItemContentImg.getImgName());
	            }

	            String oriImgName = itemContentImgFile.getOriginalFilename();
	            String imgName = fileService.uploadFile(itemContentImgLocation, oriImgName, itemContentImgFile.getBytes());
	            String imgUrl = "/images/item/content/" + imgName;
	            savedItemContentImg.updateItemContentImg(oriImgName, imgName, imgUrl);
	        }
	    }

  

}