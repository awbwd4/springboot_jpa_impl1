package jpabook.jpashop.service;

import jpabook.jpashop.item.Book;
import jpabook.jpashop.item.Item;
import jpabook.jpashop.repository.ItemRepository;
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

    @Transactional
    public Item updateItem(Long id, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(id);//em으로 가져와서 영속 상태임.

        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
//        findItem.setAuthor(param.getAuthor());
//        findItem.setIsbn(param.getIsbn());

        return findItem;

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
