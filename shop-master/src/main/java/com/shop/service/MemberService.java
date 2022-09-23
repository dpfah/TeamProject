package com.shop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.MemberFormDto;
import com.shop.dto.MemberImgDto;
import com.shop.entity.Member;
import com.shop.entity.MemberImg;
import com.shop.entity.Oqna;
import com.shop.repository.MemberImgRepository;
import com.shop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    
    private final MemberImgService memberImgService;

    private final MemberImgRepository memberImgRepository;


    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
        
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
    
    
    @Transactional(readOnly = true)
    public MemberFormDto getMemberDtl(String email){
    	
        List<MemberImg> memberImgList = memberImgRepository.findByMemberEmailOrderByIdAsc(email);
        List<MemberImgDto> memberImgDtoList = new ArrayList<>();
        for (MemberImg memberImg : memberImgList) {
            MemberImgDto memberImgDto = MemberImgDto.of(memberImg);
            memberImgDtoList.add(memberImgDto);
        }

        Member member = memberRepository.findByEmail(email);
            //    .orElseThrow(EntityNotFoundException::new);
        MemberFormDto memberFormDto = MemberFormDto.of(member);
        memberFormDto.setMemberImgDtoList(memberImgDtoList);
        return memberFormDto;
    }

    public Long updateMember(String email, MemberFormDto memberFormDto, List<MultipartFile> memberImgFileList, PasswordEncoder passwordEncoder) throws Exception{
    	//멤버 수정
        Member member = memberRepository.findById(memberFormDto.getId())
                 .orElseThrow(EntityNotFoundException::new);
        member.updateMember(memberFormDto, passwordEncoder);
        
        List<Long> memberImgIds = memberFormDto.getMemberImgIds();
        
        
		
		List<MemberImg> savedMemberImg = memberImgRepository.findByMemberEmail(email);
            //    .orElseThrow(EntityNotFoundException::new);
        
        
       if(memberImgIds.isEmpty() && savedMemberImg == null && savedMemberImg.size() == 0){ 
    	   
    	   for(int i =0; i<memberImgFileList.size();i++) {
        	MemberImg memberImg = new MemberImg();
        	memberImg.setMember(member);
        	memberImgService.saveMemberImg(memberImg, memberImgFileList.get(i));
        	}
        
       }else {
        
        //이미지 등록
        for(int i=0;i<memberImgFileList.size();i++){
       	
            memberImgService.updateMemberImg(memberImgIds.get(i), memberImgFileList.get(i));
       		}        
        }

        return member.getId();
    }
    

}