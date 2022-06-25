package jpabook.jpashop.service.query;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
public class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;// 값객체라 노출돼도 상관없음
        private List<OrderItemDto> orderItems;



//        private List<OrderItem> orderItems;
        //이것마저도 엔티티에 대한 의존임. 이것도 끊어내야함.
        // 이렇게 하면 OrderItem 엔티티가 변경됐을때 api 스펙이 변경되는건 똑같음.

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .collect(toList());


//            order.getOrderItems().stream().forEach(o -> o.getItem().getName());
//            //orderItems : OrderItem 클래스를 보면, order필드가 지연로딩으로 설정돼있다.
//            // 따라서 order.getOrderItems()를 하면 hibernate5module에 의해 프록시 객체가 들어감.
//            // 따라서 위처럼 바로 조회해줘야함(???)
//            orderItems = order.getOrderItems();
        }

}
