package com.shop.service;

import java.util.List;


import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.shop.entity.NoticeImg;
import com.shop.repository.NoticeImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeImgService {
	// application.properties 파일에 등록한 noticeImgLocation값을 불러와서 noticeImgLocation 변수에 넣어줌
	 @Value("${noticeImgLocation}")
	    private String noticeImgLocation;

	    private final NoticeImgRepository noticeImgRepository;

	    private final FileService fileService;

	    public void saveNoticeImg(NoticeImg noticeImg, MultipartFile noticeImgFile) throws Exception{
	        String oriImgName = noticeImgFile.getOriginalFilename();
	        String imgName = "";
	        String imgUrl = "";

	        //파일 업로드
	        if(!StringUtils.isEmpty(oriImgName)){
	            imgName = fileService.uploadFile(noticeImgLocation, oriImgName,
	                    noticeImgFile.getBytes());
	            imgUrl = "/images/notice/" + imgName;
	        }

	        //상품 이미지 정보 저장
	        noticeImg.updateNoticeImg(oriImgName, imgName, imgUrl);
	        noticeImgRepository.save(noticeImg);
	    }

	    public void updateNoticeImg(Long noticeImgId, MultipartFile noticeImgFile) throws Exception{
	        if(!noticeImgFile.isEmpty()){
	            NoticeImg savedNoticeImg = noticeImgRepository.findById(noticeImgId)
	                    .orElseThrow(EntityNotFoundException::new);

	            //기존 이미지 파일 삭제
	            if(!StringUtils.isEmpty(savedNoticeImg.getImgName())) {
	                fileService.deleteFile(noticeImgLocation+"/"+
	                        savedNoticeImg.getImgName());
	            }

	            String oriImgName = noticeImgFile.getOriginalFilename();
	            String imgName = fileService.uploadFile(noticeImgLocation, oriImgName, noticeImgFile.getBytes());
	            String imgUrl = "/images/notice/" + imgName;
	            savedNoticeImg.updateNoticeImg(oriImgName, imgName, imgUrl);
	        }
	    }
	    
	    public void deleteNoticeImg(Long noticePostId) throws Exception {
		    List<NoticeImg> noticeImgList = noticeImgRepository.findByNoticeId(noticePostId); //noticeImgRepository에서 noticePostId를 찾아서 noticeImg 리스트를 만들어준다.
	        
	        if(noticeImgList != null && noticeImgList.size() != 0) { // 리스트가 null이거나 리스트 사이즈가 0이 아닐때 리스트에 있는 이미지를 삭제해준다.
	        	
	        	for(NoticeImg noticeImg : noticeImgList) {
	        		 if(!StringUtils.isEmpty(noticeImg.getImgName())) { //noticeImg의 이름부분이 채워져 있으면 폴더에 저장된 파일을 삭제할 것이다.
	 	                fileService.deleteFile(noticeImgLocation+"/"+
	 	                        noticeImg.getImgName());
	 	            }
	        		noticeImgRepository.deleteByNoticeId(noticePostId);// 그리고DB에 저장 된 것을 삭제해준다.
	        		
	        	}
	        }
	}

  

}