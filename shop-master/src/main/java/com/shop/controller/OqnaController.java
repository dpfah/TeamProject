package com.shop.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.MainOqnaDto;
import com.shop.dto.OqnaFormDto;
import com.shop.dto.OqnaSearchDto;
import com.shop.entity.Oqna;
import com.shop.service.OqnaService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OqnaController {

    private final OqnaService oqnaService;
    
    @GetMapping(value = "/oqna")
    public String oqna(OqnaSearchDto oqnaSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainOqnaDto> oqnas = oqnaService.getMainOqnaPage(oqnaSearchDto, pageable);

        model.addAttribute("oqnas", oqnas);
        model.addAttribute("oqnaSearchDto", oqnaSearchDto);
        model.addAttribute("maxPage", 5);

        return "mypage/oqna";
    }
    
    @GetMapping(value = "/mypage/oqna/new")
    public String oqnaForm(Model model){
        model.addAttribute("oqnaFormDto", new OqnaFormDto());
        return "oqna/oqnaForm";
    }

    @PostMapping(value = "/mypage/oqna/new")
    public String oqnaNew(@Valid OqnaFormDto oqnaFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("oqnaImgFile") List<MultipartFile> oqnaImgFileList){

        if(bindingResult.hasErrors()){
            return "oqna/oqnaForm";
        }

        if(oqnaImgFileList.get(0).isEmpty() && oqnaFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "oqna/oqnaForm";
        }

        try {
            oqnaService.saveOqna(oqnaFormDto, oqnaImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "oqna/oqnaForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/admin/oqna/{oqnaId}")
    public String oqnaDtl(@PathVariable("oqnaId") Long oqnaId, Model model){

        try {
            OqnaFormDto oqnaFormDto = oqnaService.getOqnaDtl(oqnaId);
            model.addAttribute("oqnaFormDto", oqnaFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("oqnaFormDto", new OqnaFormDto());
            return "oqna/oqnaForm";
        }

        return "oqna/oqnaForm";
    }

    @PostMapping(value = "/admin/oqna/{oqnaId}")
    public String oqnaUpdate(@Valid OqnaFormDto oqnaFormDto, BindingResult bindingResult,
                             @RequestParam("oqnaImgFile") List<MultipartFile> oqnaImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "oqna/oqnaForm";
        }

        if(oqnaImgFileList.get(0).isEmpty() && oqnaFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 문의 이미지는 필수 입력 값 입니다.");
            return "oqna/oqnaForm";
        }

        try {
            oqnaService.updateOqna(oqnaFormDto, oqnaImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "문의 수정 중 에러가 발생하였습니다.");
            return "oqna/oqnaForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = {"/admin/oqnas", "/admin/oqnas/{page}"})
    public String oqnaManage(OqnaSearchDto oqnaSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Oqna> oqnas = oqnaService.getAdminOqnaPage(oqnaSearchDto, pageable);

        model.addAttribute("oqnas", oqnas);
        model.addAttribute("oqnaSearchDto", oqnaSearchDto);
        model.addAttribute("maxPage", 5);

        return "oqna/oqnaMng";
    }

    @GetMapping(value = "/oqna/{oqnaId}")
    public String oqnaDtl(Model model, @PathVariable("oqnaId") Long oqnaId){
        OqnaFormDto oqnaFormDto = oqnaService.getOqnaDtl(oqnaId);
        model.addAttribute("oqna", oqnaFormDto);
        return "oqna/oqnaDtl";
    }

}