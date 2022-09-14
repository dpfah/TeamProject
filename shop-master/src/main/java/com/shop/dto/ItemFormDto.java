package com.shop.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;

import com.shop.constant.ItemSellStatus;
import com.shop.constant.ItemType;
import com.shop.entity.Item;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;
    
    @NotBlank(message = "상품부제목은 필수 입력 값입니다.")
    private String itemSNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private double ori_price;
    
    private double percent;

    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String itemDetail;
    
    @NotBlank(message = "상품 요약은 필수 입력 값입니다.")
    private String itemSummary;
    
    @NotBlank(message = "상품 포인트는 필수 입력 값입니다.")
    private String point;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;
    
    private ItemType itemType;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item,ItemFormDto.class);
    }
    
    public double getPrice() {
    	return ori_price - (ori_price *percent/100);
    }

}