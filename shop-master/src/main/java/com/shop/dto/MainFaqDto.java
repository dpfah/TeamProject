package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.shop.constant.QnAType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainFaqDto {

    private Long id;

    private String faqTitle;

    private String faqReply;

    private QnAType qnaType;

    @QueryProjection
    public MainFaqDto(Long id, String faqTitle, String faqReply, QnAType qnaType){
        this.id = id;
        this.faqTitle = faqTitle;
        this.faqReply = faqReply;
        this.qnaType = qnaType;
    }

}