package com.shop.dto;

import com.shop.constant.NoticeType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NoticeSearchDto {

    private String searchDateType;

    private NoticeType noticeType;

    private String searchBy;

    private String searchQuery = "";

}