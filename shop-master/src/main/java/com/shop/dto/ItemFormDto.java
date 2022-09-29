package com.shop.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;

import com.shop.constant.ItemSellStatus;
import com.shop.constant.ItemType;
import com.shop.entity.Item;
import com.shop.entity.Member;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;
    

    private String itemSNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private int ori_price;
    
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
    
    private String createdBy;
    
    private double grade;
    
    private int commentsCount;
    
    // 댓글
    private List<ItemCommentResponseDto> itemComments = new ArrayList<>();

    //상품 대표사진 이미지, 이미지 슬라이드
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    
    
    // 상품 상세내용 이미지
    private List<ItemContentImgDto> itemContentImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();
    
    private List<Long> itemContentImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item,ItemFormDto.class);
    }
    
    public double getPrice() {
    	if(percent == 0) {
    		return Math.round(ori_price);
    	}else {
    	return Math.round(ori_price - (ori_price *percent/100));
    	}
    }




}