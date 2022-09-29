package com.shop.entity;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

// 엔티티란 데이터 베이스의 테이블에 대응하는 클래스
import com.shop.constant.ItemSellStatus;
import com.shop.constant.ItemType;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity // 클래스를 엔티티로 선언
@Table(name="item") // 엔티티와 매칭할 테이블을 지정
@Getter // lombok어노테이션을 사용하면 getter, setter, toString 등을 자동적으로 만들어줌
@Setter
@ToString
public class Item extends BaseEntity {

    @Id // 테이블의 기본키에 사용할 속성을 지정
    @Column(name="item_id") // 필드와 컬럼 매칭
    @GeneratedValue(strategy = GenerationType.AUTO) // 키 값을 생성하는 전략 명시 GenerationType.AUTO(default): (JPA 구현체가 자동으로 생성 전략 결정) 기본키 생성 전략을 AUTO로 지정
    private Long id;       //상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm; //상품명
    
    @Column(nullable = false, length = 50)
    private String itemSNm; //상품 부제목

    @Column(name="percent")
    private double percent; //할인율
    
    @Column(name="ori_price", nullable = false)
    private int ori_price; //가격
    
    @Column(name="price", nullable = false)
    private double price; //가격

    @Column(nullable = false)
    private int stockNumber; //재고수량

    @Lob // BLOB, CLOB 타입 매칭 - CLOB: 사이즈가 큰 데이터를 외부 파일로 저장하기 위한 데이터 타입. 문자형 대용량 파일을 저장하는데 사용하는 데이터 타입, BLOB: 바이너리 데이터를 DB 외부에 저장하기 위한 타입. 이미지, 사운드, 비디오 같은 멀티미디어
    // 데이터를 다룰 때 사용 할 수 있음
    @Column(nullable = false) 
    private String itemDetail; //상품 상세 설명
    
    @Column(nullable = false) 
    private String itemSummary; //상품 요약 설명
    
    @Column(nullable = false) 
    private String point; //상품 포인트

    @Enumerated(EnumType.STRING) // enum 타입 매핑
    private ItemSellStatus itemSellStatus; //상품 판매 상태
    
    @Enumerated(EnumType.STRING) // enum 타입 매핑
    private ItemType itemType; //상품 종류
    
    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc") // 댓글 정렬
    private List<ItemComment> itemComments;

    
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view; // 조회수
    
    @Column(columnDefinition = "double default 0")
    private double grade;
    
    @Column(columnDefinition = "double default 0")
    private double commentsCount;
    
    
    
    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.ori_price = itemFormDto.getOri_price();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
        this.itemType = itemFormDto.getItemType();
        this.itemSNm = itemFormDto.getItemSNm();
        this.itemSummary = itemFormDto.getItemSummary();
        this.percent = Math.round(itemFormDto.getPercent()*100)/100;
        this.point = itemFormDto.getPoint();
        
    }
    
    public void updateItemGrade(ItemFormDto itemFormDto) {
    	this.grade = itemFormDto.getGrade();
    	this.commentsCount = itemFormDto.getCommentsCount();
    }

    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }

}