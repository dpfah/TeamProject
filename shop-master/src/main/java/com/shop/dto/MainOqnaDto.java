package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainOqnaDto {

    private Long id;

    private String oqnaTitle;

    private String oqnaDetail;

    private String imgUrl;


    @QueryProjection
    public MainOqnaDto(Long id, String oqnaTitle, String oqnaDetail, String imgUrl){
        this.id = id;
        this.oqnaTitle = oqnaTitle;
        this.oqnaDetail = oqnaDetail;
        this.imgUrl = imgUrl;

    }

}