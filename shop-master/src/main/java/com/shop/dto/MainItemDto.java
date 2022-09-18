package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainItemDto {

    private Long id;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private double price;	// 적립금
    
    private double percent;	// 정가
    
    private double ori_price;	// 할인가

    @QueryProjection
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl,double price, double percent, double ori_price){
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
        this.ori_price = ori_price;
        this.percent = percent;
    }

}