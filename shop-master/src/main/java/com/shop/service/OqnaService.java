package com.shop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.MainOqnaDto;
import com.shop.dto.OqnaFormDto;
import com.shop.dto.OqnaImgDto;
import com.shop.dto.OqnaSearchDto;
import com.shop.entity.Oqna;
import com.shop.entity.OqnaImg;
import com.shop.repository.OqnaImgRepository;
import com.shop.repository.OqnaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OqnaService {

    private final OqnaRepository oqnaRepository;

    private final OqnaImgService oqnaImgService;

    private final OqnaImgRepository oqnaImgRepository;

    public Long saveOqna(OqnaFormDto oqnaFormDto, List<MultipartFile> oqnaImgFileList) throws Exception{

        //일대일 문의 등록
        Oqna oqna = oqnaFormDto.createOqna();
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

    @Transactional(readOnly = true)
    public Page<MainOqnaDto> getMainOqnaPage(OqnaSearchDto oqnaSearchDto, Pageable pageable){
        return oqnaRepository.getMainOqnaPage(oqnaSearchDto, pageable);
    }
    
}