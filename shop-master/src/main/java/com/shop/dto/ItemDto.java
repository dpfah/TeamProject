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

    private Integer price;
    
    private Integer percent;

    private String itemDetail;
    
    private String itemSummary;
    
    private String point;

    private String sellStatCd;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
    
    private String itemType;

}