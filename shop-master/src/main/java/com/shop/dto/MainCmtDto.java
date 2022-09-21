package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainCmtDto {

    private Long id;

    private String cmtTitle;

    private String cmtDetail;

    private String imgUrl;

    private String name;

    @QueryProjection
    public MainCmtDto(Long id, String cmtTitle, String cmtDetail, String imgUrl, String name){
        this.id = id;
        this.cmtTitle = cmtTitle;
        this.cmtDetail = cmtDetail;
        this.imgUrl = imgUrl;
        this.name = name;
    }

}