package com.shop.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeDto {

    private Long id;

    private String noticeType;
    
    private String noticeTitle;
    
    private String text;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}