package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.exception.NotEnoughStockException;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;


    @Test
    public void 상품주문() throws Exception {
       //given
        Member member = createMember("회원1");

        Item book = createBook("시골jpa", 10000, 10);

        int orderCount = 2;


        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        Order getOrder = orderRepository.findOne(orderId);

       //then

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 **종류** 수가 정확해야 함.", 1, getOrder.getOrderItems().size());
        assertEquals("주문한 상품 *갯수*가 정확해야 함.", 2, getOrder.getOrderItems().get(0).getCount());
        assertEquals("주문자의 이름이 정확해야함.", "회원1", getOrder.getMember().getName());
        assertEquals("주문자의 번호가 정확해야함.", member.getId(), getOrder.getMember().getId());
        assertEquals("주문가격은 가격*수량이다..", 20000
                , getOrder.getOrderItems().get(0).getOrderPrice() * getOrder.getOrderItems().get(0).getCount());
        assertEquals("주문 이후 재고 수량이 정확해야함.", 8, getOrder.getOrderItems().get(0).getItem().getStockQuantity());
    }



    @Test
    public void 주문취소() throws Exception {
       //given
        Member member = createMember("회원1");
        Item book = createBook("시골jpa", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);


        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소시 상태는 cancel이 돼야 한다.", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문 취소시 재고는 원복돼야 한다.", 10, book.getStockQuantity());


    }
    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
       //given
        Member member = createMember("회원1");
        Item book = createBook("시골jpa", 10000, 10);

        int orderCount = 11;

       //when
        orderService.order(member.getId(), book.getId(), orderCount);

       //then
        fail("재고수량 예외가 발생해야함.");

    }
    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address("서울", "망원", "123-123"));
        em.persist(member);
        return member;
    }


}
