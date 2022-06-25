package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    /** v4
     * 컬렉션은 별도로 조회
     * Query: 루트 1번, 컬렉션 N 번
     * 단건 조회에서 많이 사용하는 방식
     */
    public List<OrderQueryDto> findOrderQueryDtos() {
        //루트 조회(toOne 코드를 모두 한번에 조회)
        List<OrderQueryDto> result = findOrders();//주문들 전체 조회 후 OrderQueryDto타입으로 반환

        //루프를 돌면서 컬렉션 추가(추가 쿼리 실행)
        result.forEach(oqd -> {
            //OrderQueryDto에 있는 주문 아이디로 주문 상품들 조회, 주문 상품들을 orderItems 리스트로 반환
            List<OrderItemQueryDto> orderItems = findOrderItems(oqd.getOrderId());
            // 리스트로 반환한 orderItems를 위의 OrderQueryDto에 넣기
            oqd.setOrderItems(orderItems);
        });
        return result;
    }


    /** v5
     * 최적화
     * Query: 루트 1번, 컬렉션 1번
     * 데이터를 한꺼번에 처리할 때 많이 사용하는 방식
     *
     */
    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();

        //주문 아이디의 리스트 받아오기
        List<Long> orderIds = toOrderIds(result);

        // 주문 상품들 가져오기
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

        // OrderQueryDto에 주문 상품들 넣기(주문 아이디 기준)
        result.forEach(oqd -> oqd.setOrderItems(orderItemMap.get(oqd.getOrderId())));

        return result;

    }

    /** v6
     * Order - OrderItem - Item을 한번에 조인해서 한번에 데이터를 가져오기
     * **/
    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d" +
                                " join o.orderItems oi" +
                                " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }







    /**==========jpql 메서드================**/

    /**
     * 1:N 관계(컬렉션)를 제외한 나머지를 한번에 조회
     */
    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    /**
     * 1:N 관계인 orderItems 조회
     */
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = : orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }


    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        // 주문 상품들 가져오기
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        // 주문 아이디를 기준으로 주문 상품들 Map구조로 매핑
        Map<Long, List<OrderItemQueryDto>> orderItemMap
                = orderItems.stream().collect(groupingBy(OrderItemQueryDto::getOrderId));
        //orderId를 기준으로 Map으로 바뀜.
        // key : orderId
        // value : OrderItemQueryDto
        return orderItemMap;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        List<Long> orderIds
                = result.stream()
                .map(oqd -> oqd.getOrderId())
                .collect(toList());
        return orderIds;
    }



}