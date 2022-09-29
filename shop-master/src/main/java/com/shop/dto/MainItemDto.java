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

    private double price;	// 할인된 가격
    
    private double percent;	// 할인율
    
    private int ori_price;	// 정가
    
    private String itemSummary;		// 요약정보
    
    private double grade;

    @QueryProjection
    public MainItemDto(Long id, String itemNm, String itemDetail, String itemSummary, String imgUrl,double price, double percent, int ori_price, double grade){
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.itemSummary = itemSummary;
        this.imgUrl = imgUrl;
        this.price = price;
        this.ori_price = ori_price;
        this.percent = percent;
        this.grade = grade;
    }

}