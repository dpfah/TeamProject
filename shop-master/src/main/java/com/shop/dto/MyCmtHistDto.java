package com.shop.dto;

import java.time.format.DateTimeFormatter;

import com.shop.entity.Cmt;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyCmtHistDto {
	
    private Long cmtId;
    
    private String cmtTitle; //제목
    
    private String cmtDate; //등록날짜
    
    private String cmtReply;
    
    private int view;	// 조회수


    public MyCmtHistDto(Cmt cmt){
        this.cmtId = cmt.getId();
        this.cmtTitle = cmt.getCmtTitle();
		this.cmtDate = cmt.getCmtDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.cmtReply = cmt.getCmtReply();
        this.view = cmt.getView();
    }

}