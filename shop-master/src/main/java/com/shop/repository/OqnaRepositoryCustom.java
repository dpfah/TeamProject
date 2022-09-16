package com.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shop.dto.MyOqnaHistDto;
import com.shop.dto.OqnaSearchDto;
import com.shop.entity.Oqna;

public interface OqnaRepositoryCustom {

    Page<Oqna> getAdminOqnaPage(OqnaSearchDto oqnaSearchDto, Pageable pageable);

    

}