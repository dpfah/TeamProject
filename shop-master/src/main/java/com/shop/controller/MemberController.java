package com.shop.controller;


import java.security.Principal;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.MemberFormDto;
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
    
    @PreAuthorize("isAnonymous")
    @GetMapping("/find_id")
    public String findId(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
    	return "/member/memberFindId";
    }

    // REST 방식에서 값을 읽어내는 동작은 GET이다. ★ 매핑 주소 find_id 아닌 find/id으로 주는 것 주의!
    @ResponseBody
    @PreAuthorize("isAnonymous")
    @GetMapping("/find/id")
    public ResponseEntity<String> findId(MemberFormDto memberFormDto) {
        String id = memberService.findId(memberFormDto);
        if(id == null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("아이디를 찾지 못했습니다.");
        return ResponseEntity.ok(id);
    }

    
    //멤버 업데이트 페이지 가져오기
    @GetMapping(value = "/update")// email로 정한 이유는 세션에서 받아온 값으로 접속하기 위해서.
    public String memberDtl(Model model, Principal principal){

        try {
			MemberFormDto memberFormDto = memberService.getMemberDtl(principal.getName());
            model.addAttribute("memberFormDto", memberFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 아이디 입니다.");
            model.addAttribute("memberFormDto", new MemberFormDto());
            return "member/memberUpdateForm";
        }

        return "member/memberUpdateForm";
    }

  //멤버 업데이트 페이지에서 수정 내용 
    @PostMapping(value = "/update/{email}")
    public String memberUpdate(@PathVariable("email") String email, @Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                             @RequestParam("memberImgFile") List<MultipartFile> memberImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "member/memberUpdateForm";
        }

        if(memberImgFileList.get(0).isEmpty()){
            model.addAttribute("errorMessage", "프로필이미지는 필수 입력 값 입니다.");
            return "member/memberUpdateForm";
        }

        try {
			// 자신의 개인정보수정을 위해 세션에 저장된 이메일로 개인정보를 찾아내서 수정함.
			memberService.updateMember(email, memberFormDto, memberImgFileList, passwordEncoder);
        } catch (Exception e){
            model.addAttribute("errorMessage", "개인정보 수정 중 에러가 발생하였습니다.");
            return "member/memberUpdateForm";
        }

        return "redirect:/members/dtl";
    }
    
    // 멤버 상세보기
    @GetMapping(value = "/dtl") //세션
    public String memberDtl1(Model model,Principal principal){
    	// 세션에서 이메일로 받아와서 처리
        MemberFormDto memberFormDto = memberService.getMemberDtl(principal.getName());
        model.addAttribute("member", memberFormDto);
        return "member/memberDtl";
    }
    
//    // 멤버 상세보기
//    @GetMapping(value = "/dtl/{email}") //세션
//    public String memberDtl1(Model model, @PathVariable("email") String email){
//        MemberFormDto memberFormDto = memberService.getMemberDtl(email);
//        model.addAttribute("member", memberFormDto);
//        return "member/memberDtl";
//    }
    
    
//    @DeleteMapping(value = "/delete/{email}")
//    public @ResponseBody ResponseEntity deleteMember(@PathVariable("email") String email, Principal principal, @Valid MemberFormDto memberFormDto, BindingResult bindingResult,
//            Model model) throws Exception
//    {
//    
//		//    	if(!principal.getName().equals(email)) {
////    		return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
////    	}
////    	
//    	memberService.deleteMember(email);
//    	return new ResponseEntity<String>(email, HttpStatus.OK);
//    }


}