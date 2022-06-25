package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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
    public List<Order> findAll(OrderSearch orderSearch) {
        return em.createQuery("select o " +
                        "from Order o join o.member m" +
                        "where o.status = :status" +
                        "and m.name like :name"
                , Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000) //최대 1000건
                .getResultList();
    }

    // 주문 상태로 주문 검색
    public List<Order> findAllByString(OrderSearch orderSearch) {
        //language=JPQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }
    /** /////////////// 멤버 가져오기 xToOne /////////////// **/
    /** fetch join 쓰기**/
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
        "select o from Order o" +
                " join fetch o.member m" +//Order를 가져올때 member까지 한번에 가져옴
                " join fetch o.delivery d", Order.class)
                .getResultList();
       //fetch join을 써서 지연로딩(LAZY)인 멤버들의 값을 다 채워서 가져옴(프록시 아님)
    }

    /**
     * DTO에 맞춰서 DB에서 가져오기.
     * 일반적인 sql처럼 원하는 값만 select 해온다.
     * **/
    public List<OrderSimpleQueryDto> findOrderDtos() {
       return em.createQuery(
       "select new jpabook.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
               "from Order o" +
               " join o.member m" +
               " join o.delivery d", OrderSimpleQueryDto.class)
        .getResultList();
    }


    /** /////////////// 상품 가져오기 xToMany /////////////// **/
    /**
     * OrderItem 리스트, Fetch join으로 가져오기.
     * **/
    public List<Order> findAllWithItem() {
        return em.createQuery(
                        "select distinct o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d" +
                                " join fetch o.orderItems oi" +
                                " join fetch oi.item i", Order.class)
                //distinct : sql과 달리, 루트 엔티티(Order)에서 중복이 발생하면
                // 그 중복을 제거해서 컬렉션에 넣어준다.
                // sql은 모든 칼럼값이 같아야 중복 제거가 되는데
                // jdql은 중복된것만 제거해줌.
                .setFirstResult(1)
                .setMaxResults(100)
                .getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                        "select o from Order o" +
                                " join fetch o.member m" +//Order를 가져올때 member까지 한번에 가져옴
                                " join fetch o.delivery d", Order.class)
                .setFirstResult(offset).setMaxResults(limit)
                .getResultList();
        //fetch join을 써서 지연로딩(LAZY)인 멤버들의 값을 다 채워서 가져옴(프록시 아님)
    }
}
