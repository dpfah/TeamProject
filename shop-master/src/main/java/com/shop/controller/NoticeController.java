package com.shop.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//import com.shop.dto.NoticeFormDto;
//import com.shop.entity.Notice;
//import com.shop.service.NoticeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NoticeController {

//     private final NoticeService noticeService;

    @GetMapping(value = "/notice")
    public String notice(){
        return "notice/noticeMain";
    }
    
    @GetMapping(value = "/notice/post")
    public String noticePost(){
        return "notice/noticePost";
    }
    
//    @GetMapping(value = "/admin/notice/new")
//    public String itemForm(Model model){
//        model.addAttribute("noticeFormDto", new NoticeFormDto());
//        return "notice/noticeForm";
//    }
//    
//    @GetMapping(value = {"/admin/notices", "/admin/notices/{page}"})
//    public String noticeManage(@PathVariable("page") Optional<Integer> page, Model model){
//
//        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
//        Page<Notice> notices = noticeService.getAdminNoticePage(pageable);
//
//        model.addAttribute("notices", notices);
//        model.addAttribute("maxPage", 5);
//
//        return "notice/noticeMng";
//    }

}