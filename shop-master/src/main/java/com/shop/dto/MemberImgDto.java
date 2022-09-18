package com.shop.dto;

import org.modelmapper.ModelMapper;

import com.shop.entity.MemberImg;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;
    
    private static ModelMapper modelMapper = new ModelMapper();

    public static MemberImgDto of(MemberImg memberImg) {
        return modelMapper.map(memberImg, MemberImgDto.class);
    }

}