package com.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shop.dto.BreadItemDto;
import com.shop.dto.CakeItemDto;
import com.shop.dto.CookiesItemDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    
    //Bread, Cookies, Cake 각각페이징
    Page<BreadItemDto> getBreadItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    
    Page<CookiesItemDto> getCookiesItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    
    Page<CakeItemDto> getCakeItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}