package com.shop.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import com.shop.entity.Member;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberFormDto {
	
	private Long id;

    private String name;
    
    private String birth;
    
    private String gender;
    
    private String phone;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    
    private String password;


    private String address;
    
    private List<MemberImgDto> memberImgDtoList = new ArrayList<>();

    private List<Long> memberImgIds = new ArrayList<>();
    
    private static ModelMapper modelMapper = new ModelMapper();

    public static MemberFormDto of(Member member){
        return modelMapper.map(member, MemberFormDto.class);
    }
    

}