package com.shop.dto;


import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import org.modelmapper.ModelMapper;

import com.shop.constant.NoticeType;
import com.shop.entity.Notice;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NoticeFormDto {

    private Long id;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String noticeTitle;
    
    private String noticeText;
    
    private NoticeType noticeType;
    
    private LocalDateTime regTime;

    private static ModelMapper modelMapper = new ModelMapper();

    public Notice createNotice(){
        return modelMapper.map(this, Notice.class);
    }

    public static NoticeFormDto of(Notice notice){
        return modelMapper.map(notice,NoticeFormDto.class);
    }
    
}