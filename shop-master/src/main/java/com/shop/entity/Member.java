package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.ItemFormDto;
import com.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="member")
@Getter @Setter
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    
    private String birth;
    
    private String gender;
    
    private String phone;

    @Column(unique = true, name="member_email")
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;
    
    public void updateMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        this.name = memberFormDto.getName();
        this.birth = memberFormDto.getBirth();
        this.gender = memberFormDto.getGender();
        this.phone = memberFormDto.getPhone();
        this.email = memberFormDto.getEmail();
        this.password = passwordEncoder.encode(memberFormDto.getPassword());
        this.address = memberFormDto.getAddress();
    }

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setBirth(memberFormDto.getBirth());
        member.setGender(memberFormDto.getGender());
        member.setPhone(memberFormDto.getPhone());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }


}
