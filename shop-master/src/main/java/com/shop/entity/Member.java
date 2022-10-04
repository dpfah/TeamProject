package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.HashSet;

import javax.persistence.*;

@Entity
@Table(name="member")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    

    // 로그아웃-토큰삭제
//    int deletePersistentLogins(MemberCustom memberCustom);

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
        if (memberFormDto.getName().equals("admin")) {
        	member.setRole(Role.ADMIN);
        } else {
        	member.setRole(Role.USER);
        }
        
        return member;
    }
    
    private boolean del;
    
    private boolean social;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Role> roleSet = new HashSet<>();
    
    public void changePassword(String password) {
    	this.password = password;
    }
    
    public void changeEmail(String email) {
    	this.email = email;
    }

    public void changeDel(boolean del) {
    	this.del = del;
    }

    public void addRole(Role role) {
    	this.roleSet.add(role);
    }

    public void clearRoles() {
    	this.roleSet.clear();
    }

    public void changeSocial(boolean social) {
    	this.social = social;
    }

    
}
