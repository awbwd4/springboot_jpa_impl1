package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)//junit에게 스프링 관련된 테스트를 한다고 알려줌
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void testMember() throws Exception{
        //given

        //        ApplicationContext ac = new AnnotationConfigApplicationContext(Member.class);
        //        Member member = ac.getBean(Member.class);
        //        member.setUsername("memberA");
                //멤버는 스프링 빈으로 관리되지 않음. 싱글톤x
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long savedId = memberRepository.save(member);
        Member foundMember = memberRepository.find(savedId);

        //then

        Assertions.assertThat(foundMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(foundMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(foundMember).isEqualTo(member);

        System.out.println("foundMember == member" + (foundMember==member));


    }


}