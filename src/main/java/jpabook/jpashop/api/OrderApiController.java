package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    /**
     * Order에서 OneToMany 관계인 OrderItem리스트까지 api에 포함한다.
     * **/

    /**
     * /////////////////api 메서드/////////////////
     * **/

    //v1 엔티티 직접 노출
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {

        //엔티티인 Order를 직접 노출하는 방식, 사용하면 안됨.

        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }


    //v2 엔티티를 DTO로 변환, n+1문제 발생
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> result = orders.stream().map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }


    //v3 엔티티를 DTO로 변환, fetch join으로 최적화
    @GetMapping("/api/v3/orders")
    public List<OrderDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(toList());
        return result;
    }

    //v3.1 엔티티를 DTO로 변환, 페이징처리/한계돌파(batch)
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset ,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {

        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(toList());

        return result;
    }


    //v4. 엔티티가 아닌 특정 화면에 fit한 데이터를 내보낼때.
    // 엔티티를 호출하는게 아니라 JPA에서 바로 DTO로
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }


    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    /**
     * Order - OrderItem - Item을 한번에 조인해서 한번에 데이터를 가져오기
     * **/
    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(toList());
    }








    /**
     * /////////////////Order DTO/////////////////
     *
     * **/

    @Data
    static class OrderDto{

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

    @Getter
    static class OrderItemDto{

        private String itemName; //상품명
        private int orderPrice; //주문가격
        private int count;  //주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }




}
