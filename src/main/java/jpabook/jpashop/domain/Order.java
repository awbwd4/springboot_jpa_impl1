package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 주문n - 멤버 1
    @JoinColumn(name = "member_id")// 매핑될 fk
    private Member member;
    /**
     * FetchType.LAZY : 지연로딩
     * 지연로딩이 되면 Member객체의 정보는 일단 가져오지 않고
     * 나머지 필드들의 정보만 가져온다
     * Member객체에 null을 할 수는 없으므로
     * proxy객체를 넣어서 가져온다.
     * 그 proxy가 "ByteBuddyInterceptor"
     *
     * jackson 라이브러리에서는 이 프록시를 읽을 수 없다.
     * 따라서 지연로딩인 경우에는
     * jackson 라이브러리가  아무것도 하지 않도록 해야함.
     * hibernate5module 빈 등록
     *
     * **///




//    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문일시

    private OrderStatus status;//주문상태 (ORDER, CANCEL)


    //연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);

    }

    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }


    //==비즈니스 로직==//
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소 불가");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    //전체 주문가격 조회
    public int getTotalPrice() {
        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
//        int totalPrice = 0;
//        for(OrderItem orderItem:orderItems){
//            totalPrice += orderItem.getTotalPrice();
//        }
//        return totalPrice;
    }

}
