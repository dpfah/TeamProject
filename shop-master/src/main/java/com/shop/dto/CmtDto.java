package com.shop.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CmtDto {

    private Long id;

    private String cmtTitle;
   
    private String cmtDetail;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}