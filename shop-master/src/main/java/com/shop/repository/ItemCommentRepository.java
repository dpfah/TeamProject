package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.entity.ItemComment;

public interface ItemCommentRepository extends JpaRepository<ItemComment, Long> {

	List<ItemComment> findByItemIdOrderByIdDesc(Long itemId);
	
	



}