package com.shop.dto;

import org.modelmapper.ModelMapper;

import com.shop.entity.CmtImg;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CmtImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static CmtImgDto of(CmtImg oQnAImg) {
        return modelMapper.map(oQnAImg,CmtImgDto.class);
    }

}