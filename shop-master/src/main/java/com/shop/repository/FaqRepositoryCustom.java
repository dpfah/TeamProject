package com.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shop.dto.FaqSearchDto;
import com.shop.dto.MainFaqDto;
import com.shop.entity.Faq;

public interface FaqRepositoryCustom {

    Page<Faq> getAdminFaqPage(FaqSearchDto faqSearchDto, Pageable pageable);

    Page<MainFaqDto> getMainFaqPage(FaqSearchDto faqSearchDto, Pageable pageable);

}