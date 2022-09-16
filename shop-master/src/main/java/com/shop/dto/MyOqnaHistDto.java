package com.shop.dto;

import java.time.format.DateTimeFormatter;

import com.querydsl.core.annotations.QueryProjection;
import com.shop.constant.OqnaStatus;
import com.shop.entity.Oqna;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyOqnaHistDto {

    private Long id;

    private String oqnaTitle;

    private String oqnaDetail;
    
    private String oqnaDate; //문의날짜
    
    private OqnaStatus oqnaStatus; //문의 상태

    private String imgUrl;


    @QueryProjection
    public MyOqnaHistDto(Oqna oqna, Long id, String oqnaTitle, String oqnaDetail, String imgUrl){
        this.id = id;
        this.oqnaTitle = oqnaTitle;
        this.oqnaDetail = oqnaDetail;
        this.imgUrl = imgUrl;
		this.oqnaDate = oqna.getOqnaDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.oqnaStatus = oqna.getOqnaStatus();

    }

}