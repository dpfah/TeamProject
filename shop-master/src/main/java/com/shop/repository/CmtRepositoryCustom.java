package com.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shop.dto.CmtSearchDto;
import com.shop.dto.MainCmtDto;
import com.shop.entity.Cmt;

public interface CmtRepositoryCustom {

    Page<Cmt> getAdminCmtPage(CmtSearchDto cmtSearchDto, Pageable pageable);

	Page<MainCmtDto> getMainCmtPage(CmtSearchDto cmtSearchDto, Pageable pageable);

    

}