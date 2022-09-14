package com.shop.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OqnaDto {

    private Long id;

    private String oqnaTitle;
   
    private String oqnaDetail;

    private String qnaType;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}