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

import com.shop.dto.CmtFormDto;
import com.shop.dto.CmtSearchDto;
import com.shop.dto.MyCmtHistDto;
import com.shop.entity.Cmt;
import com.shop.service.CmtService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CmtController {

    private final CmtService cmtService;
    
//    @GetMapping(value = "/")
//    public String main(CmtSearchDto cmtSearchDto, Optional<Integer> page, Model model){
//
//        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
//        Page<MainCmtDto> cmts = cmtService.getMainCmtPage(cmtSearchDto, pageable);
//
//        model.addAttribute("cmts", cmts);
//        model.addAttribute("cmtSearchDto", cmtSearchDto);
//        model.addAttribute("maxPage", 2);
//
//        return "cmt/MainCmt";
//    }
    
    //마이페이지에서 1:1문의 리스트
    @GetMapping(value = {"/cmts", "/cmts/{page}"})
    public String cmtHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){
    	
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<MyCmtHistDto> myCmtsHistDtoList = cmtService.getCmtList(principal.getName(), pageable);
       
        // 세션에서 받아온 이메일
        model.addAttribute("email", principal.getName());
        model.addAttribute("cmts",	myCmtsHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "cmt/MyCmtHist";
    }
    
    
    // 마이페이지에서 1:1 문의 취소하기
    @PostMapping("/cmt/{cmtId}/cancel")
    public @ResponseBody ResponseEntity cancelCmt(@PathVariable("cmtId") Long cmtId , Principal principal){

        if(!cmtService.validateCmt(cmtId, principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cmtService.cancelCmt(cmtId);
        return new ResponseEntity<Long>(cmtId, HttpStatus.OK);
    }
    


    
    // 1:1문의 작성
    @GetMapping(value = "/cmt/new")
    public String cmtForm(Model model, Principal principal){
    	model.addAttribute("email", principal.getName());
        model.addAttribute("cmtFormDto", new CmtFormDto());
        return "cmt/cmtForm";
    }
    
    // 1:1문의 작성
    @PostMapping(value = "/cmt/new")
    public String cmtNew(@Valid CmtFormDto cmtFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("cmtImgFile") List<MultipartFile> cmtImgFileList, Principal principal){

        if(bindingResult.hasErrors()){
            return "cmt/cmtForm";
        }

//        if(cmtImgFileList.get(0).isEmpty() && cmtFormDto.getId() == null){
//            model.addAttribute("errorMessage", "첫번째 이미지는 필수 입력 값 입니다.");
//            return "cmt/cmtForm";
//        }
        
        // email을 세션에서 받아온다.
        String email = principal.getName();
        //Long cmtWriter;
        
        try {
			
			cmtService.saveCmt(email, cmtFormDto, cmtImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "질문 등록 중 에러가 발생하였습니다.");
            return "cmt/cmtForm";
        }

        return "redirect:/";
    }

    //1:1 문의 업데이트 페이지 가져오기
    @GetMapping(value = "/cmt/update/{cmtId}")
    public String cmtDtl(@PathVariable("cmtId") Long cmtId, Model model){

        try {
            CmtFormDto cmtFormDto = cmtService.getCmtDtl(cmtId);
            model.addAttribute("cmtFormDto", cmtFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("cmtFormDto", new CmtFormDto());
            return "cmt/cmtForm";
        }

        return "cmt/cmtForm";
    }

  //1:1 문의 수정한 내용 
    @PostMapping(value = "/cmt/update/{cmtId}")
    public String cmtUpdate(@Valid CmtFormDto cmtFormDto, BindingResult bindingResult,
                             @RequestParam("cmtImgFile") List<MultipartFile> cmtImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "cmt/cmtForm";
        }

//        if(cmtImgFileList.get(0).isEmpty() && cmtFormDto.getId() == null){
//            model.addAttribute("errorMessage", "첫번째 문의 이미지는 필수 입력 값 입니다.");
//            return "cmt/cmtForm";
//        }

        try {
            cmtService.updateCmt(cmtFormDto, cmtImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "문의 수정 중 에러가 발생하였습니다.");
            return "cmt/cmtForm";
        }

        return "redirect:/";
    }

    //관리자 페이지에서 전체 1:1 문의 목록보기
    @GetMapping(value = {"/admin/cmts", "/admin/cmts/{page}"})
    public String cmtManage(CmtSearchDto cmtSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Cmt> cmts = cmtService.getAdminCmtPage(cmtSearchDto, pageable);

        model.addAttribute("cmts", cmts);
        model.addAttribute("cmtSearchDto", cmtSearchDto);
        model.addAttribute("maxPage", 5);

        return "cmt/cmtMng";
    }

    // 1:1문의 상세보기
    @GetMapping(value = "/cmt/dtl/{cmtId}")
    public String cmtDtl(Model model, @PathVariable("cmtId") Long cmtId){
        CmtFormDto cmtFormDto = cmtService.getCmtDtl(cmtId);
        model.addAttribute("cmt", cmtFormDto);
        return "cmt/cmtDtl";
    }
    
    // 삭제하기
    @DeleteMapping(value = "/cmt/delete/{cmtId}")
    public @ResponseBody ResponseEntity deleteCmt(@PathVariable("cmtId")
    	Long cmtId, Principal principal, @Valid CmtFormDto cmtFormDto, BindingResult bindingResult,
            Model model) throws Exception
    {
    
    	if(!cmtService.validateCmt(cmtId, principal.getName())) {
    		return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
    	}
    	
    	cmtService.deleteCmt(cmtId);
    	return new ResponseEntity<Long>(cmtId, HttpStatus.OK);
    }
    


}