package com.shop.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqDto {

    private Long id;

    private String faqTitle;
    
    private String faqReply;
    
    private String qnaType;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}