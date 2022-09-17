package com.shop.dto;

import org.modelmapper.ModelMapper;

import com.shop.entity.ItemContentImg;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemContentImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;


    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemContentImgDto of(ItemContentImg itemContentImg) {
        return modelMapper.map(itemContentImg,ItemContentImgDto.class);
    }
}
