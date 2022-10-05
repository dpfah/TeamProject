package com.shop.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.dto.FaqFormDto;
import com.shop.dto.FaqSearchDto;
import com.shop.dto.MainFaqDto;
import com.shop.entity.Faq;
import com.shop.entity.FaqCrawling;
import com.shop.service.FaqCrawlingService;
import com.shop.service.FaqService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    private final FaqCrawlingService faqCrawlingService;
    
    @GetMapping(value = "/mainFaq")
    public String faq(FaqSearchDto faqSearchDto, Optional<Integer> page, Model model) throws IOException {
    	
        List<FaqCrawling> faqCrawlingList = faqCrawlingService.getFaqs();

        model.addAttribute("faqCrawlings", faqCrawlingList);
    	
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

        return "redirect:/admin/faqs";
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

        return "redirect:/admin/faqs";
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
    
    @DeleteMapping(value = "/faq/delete/{faqId}")
    public @ResponseBody ResponseEntity deleteFaq(@PathVariable("faqId") Long faqId, Principal principal, @Valid FaqFormDto faqFormDto, BindingResult bindingResult,
            Model model) throws Exception
    {
    	
    	faqService.deleteFaq(faqId);
    	return new ResponseEntity<Long>(faqId, HttpStatus.OK);
    }

}