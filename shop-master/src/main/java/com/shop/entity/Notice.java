package com.shop.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.shop.constant.NoticeType;
import com.shop.dto.NoticeFormDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity // 클래스를 엔티티로 선언
@Table(name="notice") // 엔티티와 매칭할 테이블을 지정
@Getter // lombok어노테이션을 사용하면 getter, setter, toString 등을 자동적으로 만들어줌
@Setter
@ToString
public class Notice extends BaseEntity{
 	@Id // 테이블의 기본키에 사용할 속성을 지정
    @Column(name="notice_post_id") // 필드와 컬럼 매칭
    @GeneratedValue(strategy = GenerationType.AUTO) // 키 값을 생성하는 전략 명시 GenerationType.AUTO(default): (JPA 구현체가 자동으로 생성 전략 결정) 기본키 생성 전략을 AUTO로 지정
    private Long id;       //고유 번호

    @Column(nullable = false, length = 45)
    private String noticeTitle; //
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Lob // BLOB, CLOB 타입 매칭 - CLOB: 사이즈가 큰 데이터를 외부 파일로 저장하기 위한 데이터 타입. 문자형 대용량 파일을 저장하는데 사용하는 데이터 타입, BLOB: 바이너리 데이터를 DB 외부에 저장하기 위한 타입. 이미지, 사운드, 비디오 같은 멀티미디어
    // 데이터를 다룰 때 사용 할 수 있음
    private String noticeText; // 공지 내용

    @Enumerated(EnumType.STRING) // enum 타입 매핑
    private NoticeType noticeType; // 공지 타입

    
    public void updateNotice(NoticeFormDto noticeFormDto){
        this.noticeTitle = noticeFormDto.getNoticeTitle();
        this.noticeText = noticeFormDto.getNoticeText();
        this.noticeType = noticeFormDto.getNoticeType();
    }


}
