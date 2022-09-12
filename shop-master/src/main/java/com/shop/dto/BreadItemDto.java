package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.shop.constant.ItemType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BreadItemDto {

    private Long id;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;
    
    private ItemType itemType;


    @QueryProjection
    public BreadItemDto(Long id, String itemNm, String itemDetail, String imgUrl,Integer price,ItemType itemType){
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
        this.itemType = itemType;

    }

}