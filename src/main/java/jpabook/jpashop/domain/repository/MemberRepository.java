package jpabook.jpashop.domain.repository;


import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext //jpa를 사용하기 위한 엔티티 매니저, 스프링이 주입해준다.
    private EntityManager em;

    // 멤버 등록(저장)
    public void save(Member member) {
        em.persist(member);
    }
    // 멤버 단건 조회
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
    // 멤버 다건 조회
    public List<Member> findAll() { // 전부가져오는건 jpql을 사용해야 한다.
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    // 멤버 이름으로 검색
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }




}
