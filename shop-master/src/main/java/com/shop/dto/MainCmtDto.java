package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainCmtDto {

    private Long id;

    private String cmtNm;

    private String cmtDetail;

    private String imgUrl;

    private double price;	// 적립금
    
    private double percent;	// 정가
    
    private double ori_price;	// 할인가
    
    private String cmtSummary;		// 요약정보

    @QueryProjection
    public MainCmtDto(Long id, String cmtNm, String Detail, String cmtSummary, String imgUrl,double price, double percent, double ori_price){
        this.id = id;
        this.cmtNm = cmtNm;
        this.cmtDetail = cmtDetail;
        this.imgUrl = imgUrl;
        this.price = price;
        this.ori_price = ori_price;
        this.percent = percent;
        this.cmtSummary = cmtSummary;
    }

}