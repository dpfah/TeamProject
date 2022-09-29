package com.shop.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.shop.entity.Item;
import com.shop.entity.ItemComment;
import com.shop.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemCommentRequestDto {
    private Long id;
    private String itemComment;
    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")); //날짜를 포맷하면 필드 타입은 String이 된다.
    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private Member member;
    private Item item;
    private int itemRating;

    /* Dto -> Entity */
    public ItemComment toEntity() {
        ItemComment itemComments = ItemComment.builder()
                .id(id)
                .itemComment(itemComment)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .itemRating(itemRating)
                .member(member)
                .item(item)
                .build();

        return itemComments;
    }
}
