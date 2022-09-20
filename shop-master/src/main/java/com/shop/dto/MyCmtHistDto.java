package com.shop.dto;

import java.time.format.DateTimeFormatter;

import com.shop.constant.CmtStatus;
import com.shop.entity.Cmt;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyCmtHistDto {
	
    private Long cmtId;
    
    private String cmtTitle; //제목
    
    private String cmtDate; //문의날짜
    
    private CmtStatus cmtStatus; //문의 유형
    
    private String cmtReply;


    public MyCmtHistDto(Cmt cmt){
        this.cmtId = cmt.getId();
        this.cmtTitle = cmt.getCmtTitle();
		this.cmtDate = cmt.getCmtDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.cmtStatus = cmt.getCmtStatus();
        this.cmtReply = cmt.getCmtReply();

    }

}