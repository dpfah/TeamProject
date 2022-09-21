package com.shop.controller;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.shop.dto.FaqFormDto;
import com.shop.dto.FaqSearchDto;
import com.shop.dto.MainFaqDto;
import com.shop.entity.Faq;
import com.shop.service.FaqService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;
    
    
    @GetMapping(value = "/mainFaq")
    public String faq(FaqSearchDto faqSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainFaqDto> faqs = faqService.getMainFaqPage(faqSearchDto, pageable);

        model.addAttribute("faqs", faqs);
        model.addAttribute("faqSearchDto", faqSearchDto);
        model.addAttribute("maxPage", 5);

        return "faq/mainFaq";
    }
    
    @GetMapping(value = "/admin/faq/new")
    public String faqForm(Model model){
        model.addAttribute("faqFormDto", new FaqFormDto());
        return "faq/faqForm";
    }

    @PostMapping(value = "/admin/faq/new")
    public String faqNew(@Valid FaqFormDto faqFormDto, BindingResult bindingResult,
                          Model model){

        if(bindingResult.hasErrors()){
            return "faq/faqForm";
        }


        try {
            faqService.saveFaq(faqFormDto);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "faq/faqForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/admin/faq/{faqId}")
    public String faqDtl(@PathVariable("faqId") Long faqId, Model model){

        try {
            FaqFormDto faqFormDto = faqService.getFaqDtl(faqId);
            model.addAttribute("faqFormDto", faqFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 FAQ 입니다.");
            model.addAttribute("faqFormDto", new FaqFormDto());
            return "faq/faqForm";
        }

        return "faq/faqForm";
    }

    @PostMapping(value = "/admin/faq/{faqId}")
    public String faqUpdate(@Valid FaqFormDto faqFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "faq/faqForm";
        }


        try {
            faqService.updateFaq(faqFormDto);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "faq/faqForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = {"/admin/faqs", "/admin/faqs/{page}"})
    public String faqManage(FaqSearchDto faqSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Faq> faqs = faqService.getAdminFaqPage(faqSearchDto, pageable);

        model.addAttribute("faqs", faqs);
        model.addAttribute("faqSearchDto", faqSearchDto);
        model.addAttribute("maxPage", 5);

        return "faq/faqMng";
    }

}