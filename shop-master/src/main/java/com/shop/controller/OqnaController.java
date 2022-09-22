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

import com.shop.dto.MyOqnaHistDto;
import com.shop.dto.OqnaFormDto;
import com.shop.dto.OqnaImgDto;
import com.shop.dto.OqnaSearchDto;
import com.shop.entity.Oqna;
import com.shop.entity.OqnaImg;
import com.shop.service.OqnaService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OqnaController {

    private final OqnaService oqnaService;
    
//    @GetMapping(value = "/oqna")
//    public String oqna(OqnaSearchDto oqnaSearchDto, Optional<Integer> page, Model model){
//
//        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
//        Page<MyOqnaHistDto> oqnas = oqnaService.getMainOqnaPage(oqnaSearchDto, pageable);
//
//        model.addAttribute("oqnas", oqnas);
//        model.addAttribute("oqnaSearchDto", oqnaSearchDto);
//        model.addAttribute("maxPage", 5);
//
//        return "mypage/oqna";
//    }
    
    //마이페이지에서 1:1문의 리스트
    @GetMapping(value = {"/oqnas", "/oqnas/{page}"})
    public String oqnaHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){
    	
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<MyOqnaHistDto> myOqnasHistDtoList = oqnaService.getOqnaList(principal.getName(), pageable);
       
        // 세션에서 받아온 이메일
        model.addAttribute("email", principal.getName());
        model.addAttribute("oqnas",	myOqnasHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "oqna/MyOqnaHist";
    }
    
    
    // 마이페이지에서 1:1 문의 취소하기
    @PostMapping("/oqna/{oqnaId}/cancel")
    public @ResponseBody ResponseEntity cancelOqna(@PathVariable("oqnaId") Long oqnaId , Principal principal){

        if(!oqnaService.validateOqna(oqnaId, principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        oqnaService.cancelOqna(oqnaId);
        return new ResponseEntity<Long>(oqnaId, HttpStatus.OK);
    }
    
    
    // 1:1문의 작성
    @GetMapping(value = "/oqna/new")
    public String oqnaForm(Model model, Principal principal){
    	model.addAttribute("email", principal.getName());
        model.addAttribute("oqnaFormDto", new OqnaFormDto());
        return "oqna/oqnaForm";
    }
    
    // 1:1문의 작성
    @PostMapping(value = "/oqna/new")
    public String oqnaNew(@Valid OqnaFormDto oqnaFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("oqnaImgFile") List<MultipartFile> oqnaImgFileList, Principal principal){

        if(bindingResult.hasErrors()){
            return "oqna/oqnaForm";
        }

//        if(oqnaImgFileList.get(0).isEmpty() && oqnaFormDto.getId() == null){
//            model.addAttribute("errorMessage", "첫번째 질문 이미지는 필수 입력 값 입니다.");
//            return "oqna/oqnaForm";
//        }
        
        // email을 세션에서 받아온다.
        String email = principal.getName();
        //Long oqnaWriter;
        
        try {
			
			oqnaService.saveOqna(email, oqnaFormDto, oqnaImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "질문 등록 중 에러가 발생하였습니다.");
            return "oqna/oqnaForm";
        }

        return "redirect:/";
    }

    //1:1 문의 업데이트 페이지 가져오기
    @GetMapping(value = "/oqna/update/{oqnaId}")
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

  //1:1 문의 수정한 내용 
    @PostMapping(value = "/oqna/update/{oqnaId}")
    public String oqnaUpdate(@Valid OqnaFormDto oqnaFormDto, BindingResult bindingResult,
                             @RequestParam("oqnaImgFile") List<MultipartFile> oqnaImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "oqna/oqnaForm";
        }

//        if(oqnaImgFileList.get(0).isEmpty() && oqnaFormDto.getId() == null){
//            model.addAttribute("errorMessage", "첫번째 문의 이미지는 필수 입력 값 입니다.");
//            return "oqna/oqnaForm";
//        }

        try {
            oqnaService.updateOqna(oqnaFormDto, oqnaImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "문의 수정 중 에러가 발생하였습니다.");
            return "oqna/oqnaForm";
        }

        return "redirect:/";
    }

    //관리자 페이지에서 전체 1:1 문의 목록보기
    @GetMapping(value = {"/admin/oqnas", "/admin/oqnas/{page}"})
    public String oqnaManage(OqnaSearchDto oqnaSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Oqna> oqnas = oqnaService.getAdminOqnaPage(oqnaSearchDto, pageable);

        model.addAttribute("oqnas", oqnas);
        model.addAttribute("oqnaSearchDto", oqnaSearchDto);
        model.addAttribute("maxPage", 5);

        return "oqna/oqnaMng";
    }

    // 1:1문의 상세보기
    @GetMapping(value = "/oqna/dtl/{oqnaId}")
    public String oqnaDtl(Model model, @PathVariable("oqnaId") Long oqnaId){
        OqnaFormDto oqnaFormDto = oqnaService.getOqnaDtl(oqnaId);
        model.addAttribute("oqna", oqnaFormDto);
        return "oqna/oqnaDtl";
    }
    
    @DeleteMapping(value = "/oqna/delete/{oqnaId}")
    public @ResponseBody ResponseEntity deleteOqna(@PathVariable("oqnaId") Long oqnaId, Principal principal, @Valid OqnaFormDto oqnaFormDto, BindingResult bindingResult,
            Model model) throws Exception
    {
    
    	if(!oqnaService.validateOqna(oqnaId, principal.getName())) {
    		return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
    	}
    	
    	
    	oqnaService.deleteOqna(oqnaId);
    	return new ResponseEntity<Long>(oqnaId, HttpStatus.OK);
    }

}