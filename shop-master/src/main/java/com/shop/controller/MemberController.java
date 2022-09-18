package com.shop.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.MemberFormDto;
import com.shop.dto.OqnaFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }
    
    //멤버 업데이트 페이지 가져오기
    @GetMapping(value = "/update/{email}")// email로 정한 이유는 세션에서 받아온 값으로 접속하기 위해서.
    public String memberDtl( Long memberId, Model model, @PathVariable("email") String email){

        try {
			MemberFormDto memberFormDto = memberService.getMemberDtl(memberId, email);
            model.addAttribute("memberFormDto", memberFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 아이디 입니다.");
            model.addAttribute("memberFormDto", new MemberFormDto());
            return "member/memberUpdateForm";
        }

        return "member/memberUpdateForm";
    }

  //멤버 수정한 내용 
    @PostMapping(value = "/update/{email}")
    public String memberUpdate(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                             @RequestParam("memberImgFile") List<MultipartFile> memberImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "member/memberUpdateForm";
        }


        try {
            memberService.updateMember(memberFormDto, memberImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "개인정보 수정 중 에러가 발생하였습니다.");
            return "member/memberUpdateForm";
        }

        return "redirect:/";
    }
    
    // 멤버 상세보기
    @GetMapping(value = "/dtl/{email}") //세션
    public String memberDtl(Model model, Long memberId, @PathVariable("email") String email){
        MemberFormDto memberFormDto = memberService.getMemberDtl(memberId, email);
        model.addAttribute("member", memberFormDto);
        return "member/memberDtl";
    }
    

}