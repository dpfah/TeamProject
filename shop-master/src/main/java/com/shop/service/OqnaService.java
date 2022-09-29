package com.shop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.shop.dto.MyOqnaHistDto;
import com.shop.dto.OqnaFormDto;
import com.shop.dto.OqnaImgDto;
import com.shop.dto.OqnaSearchDto;
import com.shop.entity.Member;
import com.shop.entity.Oqna;
import com.shop.entity.OqnaImg;
import com.shop.repository.MemberRepository;
import com.shop.repository.OqnaImgRepository;
import com.shop.repository.OqnaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OqnaService {

    private final OqnaRepository oqnaRepository;
    
    private final MemberRepository memberRepository;

    private final OqnaImgService oqnaImgService;

    private final OqnaImgRepository oqnaImgRepository;

    public Long saveOqna(String email, OqnaFormDto oqnaFormDto, List<MultipartFile> oqnaImgFileList) throws Exception{

    	// 세션에서 받아온 email을 Member 데이터와 비교하여 email에 해당하는 member_id를 데이터에 넣는다.
    	Member member = memberRepository.findByEmail(email);
        //일대일 문의 등록
        Oqna oqna = Oqna.createOqna(member, oqnaFormDto);
        oqnaRepository.save(oqna);

        //이미지 등록
        for(int i=0;i<oqnaImgFileList.size();i++){
            OqnaImg oqnaImg = new OqnaImg();
			oqnaImg.setOqna(oqna);

            if(i == 0)
                oqnaImg.setRepimgYn("Y");
            else
                oqnaImg.setRepimgYn("N");

            oqnaImgService.saveOqnaImg(oqnaImg, oqnaImgFileList.get(i));
        }

        return oqna.getId();
    }

    @Transactional(readOnly = true)
    public OqnaFormDto getOqnaDtl(Long oqnaId){
        List<OqnaImg> oqnaImgList = oqnaImgRepository.findByOqnaIdOrderByIdAsc(oqnaId);
        List<OqnaImgDto> oqnaImgDtoList = new ArrayList<>();
        for (OqnaImg oqnaImg : oqnaImgList) {
            OqnaImgDto oqnaImgDto = OqnaImgDto.of(oqnaImg);
            oqnaImgDtoList.add(oqnaImgDto);
        }

        Oqna oqna = oqnaRepository.findById(oqnaId)
                .orElseThrow(EntityNotFoundException::new);
        OqnaFormDto oqnaFormDto = OqnaFormDto.of(oqna);
        oqnaFormDto.setOqnaImgDtoList(oqnaImgDtoList);
        return oqnaFormDto;
    }

    public Long updateOqna(OqnaFormDto oqnaFormDto, List<MultipartFile> oqnaImgFileList) throws Exception{
        //문의 수정
        Oqna oqna = oqnaRepository.findById(oqnaFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        oqna.updateOqna(oqnaFormDto);
        List<Long> oqnaImgIds = oqnaFormDto.getOqnaImgIds();

        //이미지 등록
        for(int i=0;i<oqnaImgFileList.size();i++){
            oqnaImgService.updateOqnaImg(oqnaImgIds.get(i),
                    oqnaImgFileList.get(i));
        }

        return oqna.getId();
    }

    @Transactional(readOnly = true)
    public Page<Oqna> getAdminOqnaPage(OqnaSearchDto oqnaSearchDto, Pageable pageable){
        return oqnaRepository.getAdminOqnaPage(oqnaSearchDto, pageable);
    }

//    @Transactional(readOnly = true)
//    public Page<MyOqnaHistDto> getMainOqnaPage(OqnaSearchDto oqnaSearchDto, Pageable pageable){
//        return oqnaRepository.getMyOqnaHistPage(oqnaSearchDto, pageable);
//    }
    
    @Transactional(readOnly = true)
    public Page<MyOqnaHistDto> getOqnaList(String email, Pageable pageable) {

        List<Oqna> oqnas = oqnaRepository.findOqnas(email, pageable);
        Long totalCount = oqnaRepository.countOqna(email); //수정

        List<MyOqnaHistDto> myOqnaHistDtos = new ArrayList<>();
        
        for (Oqna oqna : oqnas) {            
			MyOqnaHistDto myOqnaHistDto = new MyOqnaHistDto(oqna);

			myOqnaHistDtos.add(myOqnaHistDto);
        }

        return new PageImpl<MyOqnaHistDto>(myOqnaHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOqna(Long oqnaId, String email){
        Member curMember = memberRepository.findByEmail(email);
        Oqna oqna = oqnaRepository.findById(oqnaId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = oqna.getMember();

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }

        return true;
    }

    public void cancelOqna(Long oqnaId){
        Oqna oqna = oqnaRepository.findById(oqnaId)
                .orElseThrow(EntityNotFoundException::new);
        oqna.cancelOqna();
    }
    
    
   
    
    public void deleteOqna(Long oqnaId) throws Exception{
        Oqna oqna = oqnaRepository.findById(oqnaId)
                .orElseThrow(EntityNotFoundException::new);
        

//        List<OqnaImg> oqnaImgList = oqnaImgRepository.findByOqnaId(oqnaId);
//        
//        if(oqnaImgList != null && oqnaImgList.size() != 0) {
//        	 
//        	oqnaImgRepository.deleteByOqnaId(oqnaId);
//        	
//        	
//        }
        
        oqnaImgService.deleteOqnaImg(oqnaId); //oqnaImgService에서 첨부된 Img를 삭제해준다

        oqnaRepository.deleteById(oqnaId); //oqna게시글을 삭제한다.
        
    }
    


}