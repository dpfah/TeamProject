package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.entity.MemberImg;
import com.shop.entity.OqnaImg;

public interface MemberImgRepository extends JpaRepository<MemberImg, Long> {

    List<MemberImg> findByMemberEmailOrderByIdAsc(String memberEmail);
   


}