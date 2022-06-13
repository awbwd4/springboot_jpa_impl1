package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne // 주문된 상품n - 상품 1
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne // 주문된상품n - 주문1
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count; //주문 수


}
