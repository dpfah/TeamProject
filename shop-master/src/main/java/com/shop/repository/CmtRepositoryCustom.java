package com.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shop.dto.CmtSearchDto;
import com.shop.entity.Cmt;

public interface CmtRepositoryCustom {

    Page<Cmt> getAdminCmtPage(CmtSearchDto cmtSearchDto, Pageable pageable);

    

}