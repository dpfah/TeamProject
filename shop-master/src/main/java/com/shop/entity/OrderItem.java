package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    
    private int oriPrice; //원가격
    
    private int orderPrice; //주문가격

    private int count; //수량
    
    private int percent; //할인률

    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOriPrice(item.getPrice());
        orderItem.setPercent(item.getPercent());
        orderItem.setOrderPrice(orderItem.getOrderPrice());
        item.removeStock(count);
        return orderItem;
    }

    public int getOrderPrice() {
    	return oriPrice * percent/100;
    }
    public int getTotalPrice(){
        return orderPrice*count;
    }

    public void cancel() {
        this.getItem().addStock(count);
    }

}