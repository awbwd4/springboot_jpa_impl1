package jpabook.jpashop.domain.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    // 주문 저장
    public void save(Order order) {
        em.persist(order);
    }

    // 주문 단건 조회
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }



    // 회원명으로 주문 검색
//    public List<Order>

    // 주문 상태로 주문 검색




}