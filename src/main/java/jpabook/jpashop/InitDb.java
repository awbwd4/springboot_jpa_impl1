package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        // @PostConstruct
        // InitDb객체가 스프링 빈으로 다 등록이 된 뒤에 init() 메서드를 호출
        // 즉, 스프링 빈 생성이 끝난 뒤에 초기화를 해주는것.
        initService.dbInit1();
        initService.dbInit2();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        /** userA 주문**/
        public void dbInit1() {
            Member member = createMember("userA","서울", "망원", "1111");
            em.persist(member);

            Book book1 = createBook("JPA1 BOOK", 20000, 200);
            em.persist(book1);

            Book book2 = createBook("JPA2 BOOK", 40000, 300);
            em.persist(book2);

            //주문 상품 생성
            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);
            // 배송지 정보 생성
            Delivery delivery = createDelivery(member);
            //주문 생성
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);

        }



        /** userB 주문**/
        public void dbInit2() {
            Member member = createMember("userA","서울", "asdf", "1111");
            em.persist(member);

            Book book1 = createBook("SPRING1 BOOK", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("SPRING2 BOOK", 20000, 100);
            em.persist(book2);

            //주문 상품 생성
            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 1);
            // 배송지 정보 생성
            Delivery delivery = createDelivery(member);
            //주문 생성
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);

        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private Book createBook(String name, int price, int quantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(quantity);
            return book;
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }



    }



}


