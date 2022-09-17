package com.shop.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.entity.ItemContentImg;

public interface ItemContentImgRepository extends JpaRepository<ItemContentImg, Long> {

    List<ItemContentImg> findByItemIdOrderByIdAsc(Long itemId);


}