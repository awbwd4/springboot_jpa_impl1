package jpabook.jpashop;


import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository_ex {

    @PersistenceContext // 엔티티 매니저가 주입됨.
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member); // 멤버 객체 저
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
        //아마 Member.classs 타입의 저장된 객체를 id를 통해 찾으라는것 같음.
    }


}
