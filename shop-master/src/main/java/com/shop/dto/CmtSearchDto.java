package com.shop.dto;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CmtSearchDto {

    private String searchDateType;

    private String searchBy;

    private String searchQuery = "";
    

}