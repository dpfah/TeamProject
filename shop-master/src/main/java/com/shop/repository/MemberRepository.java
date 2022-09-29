package com.shop.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

//	String deleteByEmail(String email);
    
    Member findByName(String name);
    
    Member findByNameAndBirthAndGenderAndPhone(String name,String birth, String gender, String phone);
    

}