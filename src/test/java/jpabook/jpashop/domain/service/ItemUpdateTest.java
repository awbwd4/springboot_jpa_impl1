package jpabook.jpashop.domain.service;


import jpabook.jpashop.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;


    @Test
    public void updateTest() throws Exception {
        //given
        Book book = em.find(Book.class, 1L);


        //when
        book.setName("asdfadf");


        //변경감지 == dirty checking : 엔티티 변경
        //then




    }

}
