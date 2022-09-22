package com.shop.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.modelmapper.ModelMapper;

import com.shop.entity.Cmt;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CmtFormDto {

    private Long id;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String cmtTitle;

    @NotBlank(message = "상세 설명은 필수 입력 값입니다.")
    private String cmtDetail;
    
    private String cmtReply;

    private List<CmtImgDto> cmtImgDtoList = new ArrayList<>();

    private List<Long> cmtImgIds = new ArrayList<>();
    
    private String cmtDate; //질문날짜
    

    private static ModelMapper modelMapper = new ModelMapper();

	
	/* public Cmt createCmt(){ return modelMapper.map(this, Cmt.class); } */
	  
	public static CmtFormDto of(Cmt cmt){ return
	  modelMapper.map(cmt,CmtFormDto.class); }
	
    
}