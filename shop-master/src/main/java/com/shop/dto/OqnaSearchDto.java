package com.shop.dto;

import com.shop.constant.QnAType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OqnaSearchDto {

    private String searchDateType;

    private QnAType qnaType;

    private String searchBy;

    private String searchQuery = "";

}