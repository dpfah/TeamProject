package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.entity.CmtImg;

public interface CmtImgRepository extends JpaRepository<CmtImg, Long> {

    List<CmtImg> findByCmtIdOrderByIdAsc(Long cmtId);

    CmtImg findByCmtIdAndRepimgYn(Long cmtId, String repimgYn);
    
    // 삭제하기
    Long deleteByCmtId(Long cmtId);
    List<CmtImg> findByCmtId(Long cmtId);

}