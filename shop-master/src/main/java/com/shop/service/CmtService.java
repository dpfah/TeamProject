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

import com.shop.dto.CmtFormDto;
import com.shop.dto.CmtImgDto;
import com.shop.dto.CmtSearchDto;
import com.shop.dto.MainCmtDto;
import com.shop.dto.MyCmtHistDto;
import com.shop.entity.Cmt;
import com.shop.entity.CmtImg;
import com.shop.entity.Member;
import com.shop.repository.CmtImgRepository;
import com.shop.repository.CmtRepository;
import com.shop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CmtService {

    private final CmtRepository cmtRepository;
    
    private final MemberRepository memberRepository;

    private final CmtImgService cmtImgService;

    private final CmtImgRepository cmtImgRepository;

    public Long saveCmt(String email, CmtFormDto cmtFormDto, List<MultipartFile> cmtImgFileList) throws Exception{

    	// 세션에서 받아온 email을 Member 데이터와 비교하여 email에 해당하는 member_id를 데이터에 넣는다.
    	Member member = memberRepository.findByEmail(email);
        //일대일 문의 등록
        Cmt cmt = Cmt.createCmt(member, cmtFormDto);
        cmtRepository.save(cmt);

        //이미지 등록
        for(int i=0;i<cmtImgFileList.size();i++){
            CmtImg cmtImg = new CmtImg();
			cmtImg.setCmt(cmt);

            if(i == 0)
                cmtImg.setRepimgYn("Y");
            else
                cmtImg.setRepimgYn("N");

            cmtImgService.saveCmtImg(cmtImg, cmtImgFileList.get(i));
        }

        return cmt.getId();
    }


    public Long updateCmt(CmtFormDto cmtFormDto, List<MultipartFile> cmtImgFileList) throws Exception{
        //문의 수정
        Cmt cmt = cmtRepository.findById(cmtFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        cmt.updateCmt(cmtFormDto);
        List<Long> cmtImgIds = cmtFormDto.getCmtImgIds();

        //이미지 등록
        for(int i=0;i<cmtImgFileList.size();i++){
            cmtImgService.updateCmtImg(cmtImgIds.get(i),
                    cmtImgFileList.get(i));
        }

        return cmt.getId();
    }
    
    // 이미지
    @Transactional(readOnly = true)
    public CmtFormDto getCmtDtl(Long cmtId){
        List<CmtImg> cmtImgList = cmtImgRepository.findByCmtIdOrderByIdAsc(cmtId);
        List<CmtImgDto> cmtImgDtoList = new ArrayList<>();
        for (CmtImg cmtImg : cmtImgList) {
            CmtImgDto cmtImgDto = CmtImgDto.of(cmtImg);
            cmtImgDtoList.add(cmtImgDto);
        }
        
        Cmt cmt = cmtRepository.findById(cmtId)
                .orElseThrow(EntityNotFoundException::new);
        CmtFormDto cmtFormDto = CmtFormDto.of(cmt);
        cmtFormDto.setCmtImgDtoList(cmtImgDtoList);
        return cmtFormDto;
    }
    
    
    @Transactional(readOnly = true)
    public Page<Cmt> getAdminCmtPage(CmtSearchDto cmtSearchDto, Pageable pageable){
        return cmtRepository.getAdminCmtPage(cmtSearchDto, pageable);
    }

//    @Transactional(readOnly = true)
//    public Page<MyCmtHistDto> getMainCmtPage(CmtSearchDto cmtSearchDto, Pageable pageable){
//        return cmtRepository.getMyCmtHistPage(cmtSearchDto, pageable);
//    }
    
    @Transactional(readOnly = true)
    public Page<MyCmtHistDto> getCmtList(String email, Pageable pageable) {

        List<Cmt> cmts = cmtRepository.findCmts(email, pageable);
        Long totalCount = cmtRepository.countCmt(email); //빼야함

        List<MyCmtHistDto> myCmtHistDtos = new ArrayList<>();
        
        for (Cmt cmt : cmts) {            
			MyCmtHistDto myCmtHistDto = new MyCmtHistDto(cmt);

			myCmtHistDtos.add(myCmtHistDto);
        }

        return new PageImpl<MyCmtHistDto>(myCmtHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateCmt(Long cmtId, String email){
        Member curMember = memberRepository.findByEmail(email);
        Cmt cmt = cmtRepository.findById(cmtId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = cmt.getMember();

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }

        return true;
    }

    // 취소하기
    public void cancelCmt(Long cmtId){
        Cmt cmt = cmtRepository.findById(cmtId)
                .orElseThrow(EntityNotFoundException::new);
        cmt.cancelCmt();
    }

    // 삭제하기
    public void deleteCmt(Long cmtId) throws Exception{
        Cmt cmt = cmtRepository.findById(cmtId)
                .orElseThrow(EntityNotFoundException::new);
        

//        List<CmtImg> cmtImgList = cmtImgRepository.findByCmtId(cmtId);
//        
//        if(cmtImgList != null && cmtImgList.size() != 0) {
//        	 
//        	cmtImgRepository.deleteByCmtId(cmtId);
//        	
//        	
//        }
        
        cmtImgService.deleteCmtImg(cmtId);

        cmtRepository.deleteById(cmtId);
        
    }


//    @Transactional(readOnly = true)
//    public Page<MainCmtDto> getMainCmtPage(CmtSearchDto cmtSearchDto, Pageable pageable){
//        return cmtRepository.getMainCmtPage(cmtSearchDto, pageable);
//    }
//   

}