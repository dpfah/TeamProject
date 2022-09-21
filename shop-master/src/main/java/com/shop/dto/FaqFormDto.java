package com.shop.dto;


import javax.validation.constraints.NotBlank;

import org.modelmapper.ModelMapper;

import com.shop.constant.OqnaStatus;
import com.shop.constant.QnAType;
import com.shop.entity.Faq;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FaqFormDto {

    private Long id;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String faqTitle;
    
    @NotBlank(message = "답변은 필수 입력 값입니다.")
    private String faqReply;
    
    private String oqnaDate; //질문날짜
    
    private QnAType qnaType;

    private static ModelMapper modelMapper = new ModelMapper();

    public Faq createFaq(){
        return modelMapper.map(this, Faq.class);
    }

    public static FaqFormDto of(Faq faq){
        return modelMapper.map(faq,FaqFormDto.class);
    }
    
}