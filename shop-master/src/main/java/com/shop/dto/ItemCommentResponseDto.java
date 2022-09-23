package com.shop.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.modelmapper.ModelMapper;

import com.shop.entity.ItemComment;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemCommentResponseDto {

//	public ItemCommentResponseDto() {
//		
//	}
	
    private Long id;
    private String itemComment;
    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private Long memberId;
    private Long itemId;

    /* Entity -> Dto*/
//    public ItemCommentResponseDto(ItemComment itemComment) {
//        this.id = itemComment.getId();
//        this.itemComment = itemComment.getItemComment();
//        this.createdDate = itemComment.getCreatedDate();
//        this.modifiedDate = itemComment.getModifiedDate();
//        this.memberId = itemComment.getMember().getId();
//        this.itemId = itemComment.getItem().getId();
//    }
    
    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemCommentResponseDto of(ItemComment itemComment) {
        return modelMapper.map(itemComment,ItemCommentResponseDto.class);
    }


}
