package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.entity.OqnaImg;

public interface OqnaImgRepository extends JpaRepository<OqnaImg, Long> {

    List<OqnaImg> findByOqnaIdOrderByIdAsc(Long oqnaId);

    OqnaImg findByOqnaIdAndRepimgYn(Long oqnaId, String repimgYn);

	OqnaImg deleteByOqnaId(Long oqnaId);

	List<OqnaImg> findByOqnaId(Long oqnaId);

}