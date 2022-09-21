package com.shop.service;

import com.shop.entity.Oqna;
import com.shop.entity.OqnaImg;
import com.shop.repository.OqnaImgRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.util.List;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class OqnaImgService {

	  @Value("${oqnaImgLocation}")
	    private String oqnaImgLocation;

	    private final OqnaImgRepository oqnaImgRepository;

	    private final FileService fileService;

	    public void saveOqnaImg(OqnaImg oqnaImg, MultipartFile oqnaImgFile) throws Exception{
	        String oriImgName = oqnaImgFile.getOriginalFilename();
	        String imgName = "";
	        String imgUrl = "";

	        //파일 업로드
	        if(!StringUtils.isEmpty(oriImgName)){
	            imgName = fileService.uploadFile(oqnaImgLocation, oriImgName,
	                    oqnaImgFile.getBytes());
	            imgUrl = "/images/oqna/" + imgName; //c드라이브 안에 shop 폴더안에 oqna폴더를 만들어줘야함. 그리고 application.properties에 경로 설정을 해준다.
	        }

	        //상품 이미지 정보 저장
	        oqnaImg.updateOqnaImg(oriImgName, imgName, imgUrl);
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
	            String imgUrl = "/images/oqna/" + imgName;
	            savedOqnaImg.updateOqnaImg(oriImgName, imgName, imgUrl);
	        }
	    }

		public void deleteOqnaImg(Long oqnaId) {
		    List<OqnaImg> oqnaImgList = oqnaImgRepository.findByOqnaId(oqnaId);
	        
	        if(oqnaImgList != null && oqnaImgList.size() != 0) {
	        	
	        	for(OqnaImg oqnaImg : oqnaImgList) {
	        		oqnaImgRepository.deleteByOqnaId(oqnaId);
	        	}
	        	
	        	
	        }
			
	}
    
	    

}