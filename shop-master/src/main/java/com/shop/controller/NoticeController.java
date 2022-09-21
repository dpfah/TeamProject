package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    @GetMapping(value = "/notice")
    public String notice(){
        return "notice/notice_main";
    }
    
    @GetMapping(value = "/notice/post")
    public String noticePost(){
        return "notice/notice_post";
    }

}