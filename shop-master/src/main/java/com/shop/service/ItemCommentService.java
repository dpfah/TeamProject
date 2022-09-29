package com.shop.service;

import javax.transaction.Transactional;


import org.springframework.stereotype.Service;

import com.shop.dto.ItemCommentRequestDto;
import com.shop.entity.Item;
import com.shop.entity.ItemComment;
import com.shop.entity.Member;
import com.shop.repository.ItemCommentRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemCommentService {

    private final ItemCommentRepository itemCommentRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /* CREATE */
    @Transactional
    public Long itemCommentSave(String email, Long id, ItemCommentRequestDto dto) {
        Member member = memberRepository.findByEmail(email);
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + id));

        dto.setMember(member);
        dto.setItem(item);

        ItemComment itemComment = dto.toEntity();
        itemCommentRepository.save(itemComment);

        return dto.getId();
    }
    
    /* UPDATE */    
    @Transactional    
    public void update(Long id, ItemCommentRequestDto dto) { 
    	ItemComment itemComment = itemCommentRepository.findById(id).orElseThrow(() -> 
    	new IllegalArgumentException("해당 댓글이 존재하지 않습니다. " + id));         
    	itemComment.update(dto.getItemComment());    
    }
    
    /* DELETE */    
    @Transactional    
    public void delete(Long id) { 
    	ItemComment itemComment = itemCommentRepository.findById(id).orElseThrow(() ->                
    	new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + id));         
    	itemCommentRepository.delete(itemComment);   
    }
    
    
   
}

