package com.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.entity.Member;
import com.shop.entity.MemberImg;
import com.shop.entity.OqnaImg;

public interface MemberImgRepository extends JpaRepository<MemberImg, Long> {

    List<MemberImg> findByMemberEmailOrderByIdAsc(String memberEmail);

	List<MemberImg> findByMemberEmail(String email);

//	String deleteByMemberEmail(String memberEmail);
   


}