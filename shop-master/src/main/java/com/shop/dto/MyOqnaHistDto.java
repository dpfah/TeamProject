package com.shop.dto;

import java.time.format.DateTimeFormatter;

import com.shop.constant.OqnaStatus;
import com.shop.constant.QnAType;
import com.shop.entity.Oqna;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyOqnaHistDto {
	
    private Long oqnaId;
    
    private QnAType qnaType; //oQnA 타입

    private String oqnaTitle; //제목
    
    private String oqnaDate; //문의날짜
    
    private OqnaStatus oqnaStatus; //문의 유형
    
    private String oqnaReply;


    public MyOqnaHistDto(Oqna oqna){
        this.oqnaId = oqna.getId();
        this.qnaType = oqna.getQnaType();
        this.oqnaTitle = oqna.getOqnaTitle();
		this.oqnaDate = oqna.getOqnaDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.oqnaStatus = oqna.getOqnaStatus();
        this.oqnaReply = oqna.getOqnaReply();

    }

}