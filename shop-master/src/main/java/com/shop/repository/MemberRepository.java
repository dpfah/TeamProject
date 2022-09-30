package com.shop.repository;


import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shop.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	
	@EntityGraph(attributePaths = "roleSet")
	@Query("select m from Member m where m.name = :name and m.social = false")
	Optional<Member> getWithRoles(String name);

	@EntityGraph(attributePaths = "roleSet")
	Optional<Member> findByEmail(String email);

//	String deleteByEmail(String email);
    
    Member findByName(String name);
    
    Member findByNameAndBirthAndGenderAndPhone(String name,String birth, String gender, String phone);
    


}