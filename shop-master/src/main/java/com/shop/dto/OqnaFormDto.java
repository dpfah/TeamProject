package com.shop.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.modelmapper.ModelMapper;

import com.shop.constant.OqnaStatus;
import com.shop.constant.QnAType;
import com.shop.entity.Oqna;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OqnaFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String oqnaTitle;

    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String oqnaDetail;
    
    private String oqnaReply;

    private QnAType qnaType;
    
    private List<OqnaImgDto> oqnaImgDtoList = new ArrayList<>();

    private List<Long> oqnaImgIds = new ArrayList<>();
    
    private String oqnaDate; //질문날짜
    
    private OqnaStatus oqnaStatus; //질문 상태

    private static ModelMapper modelMapper = new ModelMapper();

	
	/* public Oqna createOqna(){ return modelMapper.map(this, Oqna.class); } */
	  
	public static OqnaFormDto of(Oqna oqna){ return
	  modelMapper.map(oqna,OqnaFormDto.class); }
	
    
}