package com.shop.service;


import com.shop.entity.CmtImg;
import com.shop.repository.CmtImgRepository;

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
public class CmtImgService {

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

	        //상품 이미지 정보 저장
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

		public void deleteCmtImg(Long cmtId) throws Exception {
		    List<CmtImg> cmtImgList = cmtImgRepository.findByCmtId(cmtId); //cmtImgRepository에서 cmtId를 찾아서 cmtImg 리스트를 만들어준다.
	        
	        if(cmtImgList != null && cmtImgList.size() != 0) { // 리스트가 null이거나 리스트 사이즈가 0이 아닐때 리스트에 있는 이미지를 삭제해준다.
	        	
	        	for(CmtImg cmtImg : cmtImgList) {
	        		 if(!StringUtils.isEmpty(cmtImg.getImgName())) { //cmtImg의 이름부분이 채워져 있으면 폴더에 저장된 파일을 삭제할 것이다.
	 	                fileService.deleteFile(cmtImgLocation+"/"+
	 	                        cmtImg.getImgName());
	 	            }
	        		cmtImgRepository.deleteByCmtId(cmtId);// 그리고DB에 저장 된 것을 삭제해준다.
	        		
	        	}
	        }
	}
    
	    

}