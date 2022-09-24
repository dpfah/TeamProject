package com.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "itemComments")
@Entity
public class ItemComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String itemComment; // 댓글 내용

    @Column(name = "created_date")
    @CreatedDate
    private String createdDate; // 댓글은 날짜뿐만아니라 시, 분까지 나타나게 하기 위해서 따로 포맷해줄것이다.

    @Column(name = "modified_date")
    @LastModifiedDate
    private String modifiedDate; // 위와 동일

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne // 댓글 입장에서 멤버 한명당 댓글 여러개이므로 다대일 관계이다.
    @JoinColumn(name = "member_email")
    private Member member; // 작성자
    
//    /* 댓글 수정을 위한 setter */    
//    public void update(String itemComment) { 
//    	this.itemComment = itemComment;    }

}
