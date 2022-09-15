package com.shop.dto;

import org.modelmapper.ModelMapper;

import com.shop.entity.OqnaImg;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OqnaImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static OqnaImgDto of(OqnaImg oQnAImg) {
        return modelMapper.map(oQnAImg,OqnaImgDto.class);
    }

}