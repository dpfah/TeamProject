package com.shop.dto;

import com.shop.constant.OqnaStatus;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OqnaSearchDto {

    private String searchDateType;

    private OqnaStatus searchOqnaStatus;

    private String searchBy;

    private String searchQuery = "";

}