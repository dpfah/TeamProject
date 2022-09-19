package com.shop.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.shop.entity.MemberImg;
import com.shop.repository.MemberImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberImgService {

	  @Value("${memberImgLocation}")
	    private String memberImgLocation;

	    private final MemberImgRepository memberImgRepository;

	    private final FileService fileService;

	    public void saveMemberImg(MemberImg memberImg, MultipartFile memberImgFile) throws Exception{
			String oriImgName = memberImgFile.getOriginalFilename();
	        String imgName = "";
	        String imgUrl = "";

	        //파일 업로드
	        if(!StringUtils.isEmpty(oriImgName)){
	            imgName = fileService.uploadFile(memberImgLocation, oriImgName,
	                    memberImgFile.getBytes());
	            imgUrl = "/images/member/" + imgName; //c드라이브 안에 shop 폴더안에 member폴더를 만들어줘야함. 그리고 application.properties에 경로 설정을 해준다.
	        }

	        //상품 이미지 정보 저장
	        memberImg.updateMemberImg(oriImgName, imgName, imgUrl);
	        memberImgRepository.save(memberImg);
	    }

	    public void updateMemberImg(Long memberImgId, MultipartFile memberImgFile) throws Exception{
	        if(!memberImgFile.isEmpty()){
	            MemberImg savedMemberImg = memberImgRepository.findById(memberImgId)
	                    .orElseThrow(EntityNotFoundException::new);

	            //기존 이미지 파일 삭제
	            if(!StringUtils.isEmpty(savedMemberImg.getImgName())) {
	                fileService.deleteFile(memberImgLocation+"/"+
	                        savedMemberImg.getImgName());
	            }

	            String oriImgName = memberImgFile.getOriginalFilename();
	            String imgName = fileService.uploadFile(memberImgLocation, oriImgName, memberImgFile.getBytes());
	            String imgUrl = "/images/member/" + imgName;
	            savedMemberImg.updateMemberImg(oriImgName, imgName, imgUrl);
	        }
	    }

}