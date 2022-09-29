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

    private double price;
    
    private int ori_price;
    
    private double percent;
    
    private ItemType itemType;
    
    private String itemSummary;

    private double grade;
    
    private int commentsCount;
    
    @QueryProjection
    public BreadItemDto(Long id, String itemNm, String itemDetail, String imgUrl,int ori_price ,double price,double percent, ItemType itemType, String itemSummary, double grade, int commentsCount){
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.ori_price = ori_price;
        this.price = price;
        this.percent = percent;
        this.itemType = itemType;
        this.itemSummary =itemSummary;
        this.grade = grade;
        this.commentsCount = commentsCount;
    }

}