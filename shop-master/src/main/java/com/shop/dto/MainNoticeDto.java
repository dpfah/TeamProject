package com.shop.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;
import com.shop.constant.NoticeType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainNoticeDto {

    private Long id;

    private String noticeTitle;

    private String noticeText;

    private NoticeType noticeType;

    private LocalDateTime regTime;

    @QueryProjection
    public MainNoticeDto(Long id, String noticeTitle, String noticeText, NoticeType noticeType, LocalDateTime regTime){
        this.id = id;
        this.noticeTitle = noticeTitle;
        this.noticeText = noticeText;
        this.noticeType = noticeType;
        this.regTime = regTime;
    }

}