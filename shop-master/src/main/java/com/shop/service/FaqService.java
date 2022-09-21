package com.shop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.dto.FaqFormDto;
import com.shop.dto.FaqSearchDto;
import com.shop.dto.ItemContentImgDto;
import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.entity.Faq;
import com.shop.entity.Item;
import com.shop.entity.ItemContentImg;
import com.shop.entity.ItemImg;
import com.shop.repository.FaqRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    public Long saveFaq(FaqFormDto faqFormDto) throws Exception{

        //상품 등록
        Faq faq = faqFormDto.createFaq();
        faqRepository.save(faq);


        return faq.getId();
    }


    public Long updateFaq(FaqFormDto faqFormDto) throws Exception{
        //상품 수정
        Faq faq = faqRepository.findById(faqFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        faq.updateFaq(faqFormDto);

       

        return faq.getId();
    }

    @Transactional(readOnly = true)
    public Page<Faq> getAdminFaqPage(FaqSearchDto faqSearchDto, Pageable pageable){
        return faqRepository.getAdminFaqPage(faqSearchDto, pageable);
    }

//    @Transactional(readOnly = true)
//    public Page<MainFaqDto> getMainFaqPage(FaqSearchDto faqSearchDto, Pageable pageable){
//        return faqRepository.getMainFaqPage(faqSearchDto, pageable);
//    }
    
    @Transactional(readOnly = true)
    public FaqFormDto getFaqDtl(Long faqId){
       

        Faq faq = faqRepository.findById(faqId)
                .orElseThrow(EntityNotFoundException::new);
        FaqFormDto faqFormDto = FaqFormDto.of(faq);
        return faqFormDto;
    }
    

}