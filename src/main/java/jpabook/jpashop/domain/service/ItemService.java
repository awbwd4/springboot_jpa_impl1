package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    // 저장
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // 전건 조회
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    // id로 단건 조회
    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }




}
