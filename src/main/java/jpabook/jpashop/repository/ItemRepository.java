package jpabook.jpashop.repository;

import jpabook.jpashop.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    //상품 저장
    public void save(Item item) {
        if(item.getId()==null){
            em.persist(item);
            //최초에 아이템을 저장할 때에는 id가 없음(완전히 새로 생성하는 객체)
            //따라서 persist를 사용
        }else{
            em.merge(item);
            // 업데이트 같은거
        }
    }

    // 아이템 단건 조회
    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    // 아이템 전건 조회
    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

    // 아이템 이름으로 검색
    public List<Item> findByName(String name) {
        return em.createQuery("select i from Item i where i.name = :name", Item.class)
                .setParameter("name", name)
                .getResultList();
    }



}
