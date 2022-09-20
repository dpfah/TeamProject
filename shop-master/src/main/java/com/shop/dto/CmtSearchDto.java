package com.shop.dto;

import com.shop.constant.CmtStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CmtSearchDto {

    private String searchDateType;

    private CmtStatus searchCmtStatus;
    
    private String searchBy;

    private String searchQuery = "";

}