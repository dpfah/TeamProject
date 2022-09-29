package com.shop.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {

    private Long id;

    private String itemNm;
    
    private String itemSNm;

    private int ori_price;
    
    private double price;
    
    private double percent;

    private String itemDetail;
    
    private String itemSummary;
    
    private String point;

    private String sellStatCd;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
    
    private String itemType;
    
    private double grade;
    
    private double commentsCount;

}