package com.shop.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.shop.dto.FaqFormDto;
import com.shop.dto.FaqSearchDto;
import com.shop.dto.MainFaqDto;
import com.shop.entity.Faq;
import com.shop.entity.Member;
import com.shop.repository.FaqRepository;
import com.shop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;
    
    private final MemberRepository memberRepository;

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

    @Transactional(readOnly = true)
    public Page<MainFaqDto> getMainFaqPage(FaqSearchDto faqSearchDto, Pageable pageable){
        return faqRepository.getMainFaqPage(faqSearchDto, pageable);
    }
    
    @Transactional(readOnly = true)
    public FaqFormDto getFaqDtl(Long faqId){
       

        Faq faq = faqRepository.findById(faqId)
                .orElseThrow(EntityNotFoundException::new);
        FaqFormDto faqFormDto = FaqFormDto.of(faq);
        return faqFormDto;
    }
    

    
    public void deleteFaq(Long faqId) throws Exception{
        Faq faq = faqRepository.findById(faqId)
                .orElseThrow(EntityNotFoundException::new);
        
        faqRepository.deleteById(faqId); //faq게시글을 삭제한다.
        
    }
}