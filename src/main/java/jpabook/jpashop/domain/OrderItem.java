package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//기본생성자 만드는 어노테이션, 생성메서드를 통하지 않고, 직접 setter를 통해 OrderItem객체를 만드는걸 막기 위함.
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 주문된 상품n - 상품 1
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) // 주문된 상품n - 상품 1
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 개당 주문가(쿠폰 할인등에 따라 아이템 가격과 주문 가격은 다를 수 있음)
    private int count; //주문 수


//    protected OrderItem() {
//        // 생성 메서드를 통하지 않고
//        // 직접 setter를 통해 OrderItem객체를 생성하는걸 막기 위함.
//    }

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }




    //==비즈니스 로직==//
    public void cancel() {
        getItem().addStock(count);
    }

    //==조회 로직==//

    //주문상품 전체 가격 조회
    public int getTotalPrice() {
        return orderPrice * count;
    }
}
