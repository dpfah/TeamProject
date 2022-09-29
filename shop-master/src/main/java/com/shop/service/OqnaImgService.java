package com.shop.service;


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

		public void deleteOqnaImg(Long oqnaId) throws Exception {
		    List<OqnaImg> oqnaImgList = oqnaImgRepository.findByOqnaId(oqnaId); //oqnaImgRepository에서 oqnaId를 찾아서 oqnaImg 리스트를 만들어준다.
	        
	        if(oqnaImgList != null && oqnaImgList.size() != 0) { // 리스트가 null이거나 리스트 사이즈가 0이 아닐때 리스트에 있는 이미지를 삭제해준다.
	        	
	        	for(OqnaImg oqnaImg : oqnaImgList) {
	        		 if(!StringUtils.isEmpty(oqnaImg.getImgName())) { //oqnaImg의 이름부분이 채워져 있으면 폴더에 저장된 파일을 삭제할 것이다.
	 	                fileService.deleteFile(oqnaImgLocation+"/"+
	 	                        oqnaImg.getImgName());
	 	            }
	        		oqnaImgRepository.deleteByOqnaId(oqnaId);// 그리고DB에 저장 된 것을 삭제해준다.
	        		
	        	}
	        }
	}
    
	    

}