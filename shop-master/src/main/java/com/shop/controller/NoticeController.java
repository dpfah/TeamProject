package com.shop.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.MainNoticeDto;
import com.shop.dto.NoticeFormDto;
import com.shop.dto.NoticeSearchDto;
import com.shop.entity.Notice;
import com.shop.service.NoticeImgService;
import com.shop.service.NoticeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NoticeController {

     private final NoticeService noticeService;
     
     private final NoticeImgService noticeImgService;

    @GetMapping(value = "/notice")
    public String notice(NoticeSearchDto noticeSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainNoticeDto> notices = noticeService.getMainNoticePage(noticeSearchDto, pageable);

        model.addAttribute("notices", notices);
        model.addAttribute("noticeSearchDto", noticeSearchDto);
        model.addAttribute("maxPage", 5);

        return "notice/MainNotice";
    }
    
    @GetMapping(value = "/admin/notice/new")
    public String noticeForm(Model model){
        model.addAttribute("noticeFormDto", new NoticeFormDto());
        return "notice/noticeForm";
    }
    
    @PostMapping(value = "/admin/notice/new")
    public String noticeNew(@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult, Model model, @RequestParam("noticeImgFile") List<MultipartFile> noticeImgFileList){

        if(bindingResult.hasErrors()){
            return "notice/noticeForm";
        }

        try {
            noticeService.saveNotice(noticeFormDto, noticeImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "notice/noticeForm";
        }

        return "redirect:/admin/notices";
    }

    @GetMapping(value = "/admin/notice/{noticePostId}")
    public String noticeDtl(@PathVariable("noticePostId") Long noticePostId, Model model){

        try {
            NoticeFormDto noticeFormDto = noticeService.getNoticePost(noticePostId);
            model.addAttribute("noticeFormDto", noticeFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 게시글입니다.");
            model.addAttribute("noticeFormDto", new NoticeFormDto());
            return "notice/noticeForm";
        }

        return "notice/noticeForm";
    }
    @PostMapping(value = "/admin/notice/{noticePostId}")
    public String noticeUpdate(@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult, Model model, @RequestParam("noticeImgFile") List<MultipartFile> noticeImgFileList){
        if(bindingResult.hasErrors()){
            return "notice/noticeForm";
        }

        try {
            noticeService.updateNotice(noticeFormDto, noticeImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "notice/noticeForm";
        }

        return "redirect:/admin/notices";
    }
    
    @GetMapping(value = {"/admin/notices", "/admin/notices/{page}"})
    public String noticeManage(NoticeSearchDto noticeSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Notice> notices = noticeService.getAdminNoticePage(noticeSearchDto, pageable);

        model.addAttribute("notices", notices);
        model.addAttribute("noticeSearchDto", noticeSearchDto);
        model.addAttribute("maxPage", 5);

        return "notice/noticeMng";
    }
    
    @GetMapping(value = "/notice/post/{noticePostId}")
    public String noticePost(Model model ,@PathVariable("noticePostId") Long noticePostId){
    	NoticeFormDto noticeFormDto = noticeService.getNoticePost(noticePostId);
	    model.addAttribute("notice", noticeFormDto);
	    return "notice/noticePost";
    }
    
    @DeleteMapping(value = "/notice/delete/{noticePostId}")
    public @ResponseBody ResponseEntity deleteNotice(@PathVariable("noticePostId") Long noticePostId, Principal principal, @Valid NoticeFormDto noticeFormDto, BindingResult bindingResult, Model model) throws Exception{
    	noticeImgService.deleteNoticeImg(noticePostId);
    	noticeService.deleteNotice(noticePostId);
    	return new ResponseEntity<Long>(noticePostId, HttpStatus.OK);
    }

}