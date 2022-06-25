package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByName(String name);
    //select m from Member m where m.name =?
    //메서드 이름을 보고 알아서 JPQL이 생성된다!!!!!!개쩖
}
