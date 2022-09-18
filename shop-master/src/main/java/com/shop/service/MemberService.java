package com.shop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.MemberFormDto;
import com.shop.dto.MemberImgDto;
import com.shop.entity.Member;
import com.shop.entity.MemberImg;
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
    public MemberFormDto getMemberDtl(Long memberId){
        List<MemberImg> memberImgList = memberImgRepository.findByMemberIdOrderByIdAsc(memberId);
        List<MemberImgDto> memberImgDtoList = new ArrayList<>();
        for (MemberImg memberImg : memberImgList) {
            MemberImgDto memberImgDto = MemberImgDto.of(memberImg);
            memberImgDtoList.add(memberImgDto);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new); // 상품 아이디를 통해 상품 엔티티를 조회. 존재하지 않을 때는 EntityNotFoundException발생
        MemberFormDto memberFormDto = MemberFormDto.of(member);
        memberFormDto.setMemberImgDtoList(memberImgDtoList);
        return memberFormDto;
    }

    public Long updateMember(MemberFormDto memberFormDto, List<MultipartFile> memberImgFileList) throws Exception{
        //문의 수정
        Member member = memberRepository.findById(memberFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        member.updateMember(memberFormDto);
        List<Long> memberImgIds = memberFormDto.getMemberImgIds();

        //이미지 등록
        for(int i=0;i<memberImgFileList.size();i++){
            memberImgService.updateMemberImg(memberImgIds.get(i),
                    memberImgFileList.get(i));
        }

        return member.getId();
    }


}