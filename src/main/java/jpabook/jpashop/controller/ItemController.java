package jpabook.jpashop.controller;

import jpabook.jpashop.item.Book;
import jpabook.jpashop.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    //상품 등록 화면으로 이동
    @GetMapping("/items/new")
    public String createForm(Model model) {
        log.info("create form");
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    //상품 등록 Form을 전달하여 상품 등록
    @PostMapping("/items/new")
    public String create(BookForm bookForm) {
        Book book = new Book();
//        book.setName(bookForm.getName());
//        book.setPrice(bookForm.getPrice());
//        book.setStockQuantity(bookForm.getStockQuantity());
//        book.setAuthor(bookForm.getAuthor());
//        book.setIsbn(bookForm.getIsbn());

        book.createBook(
                  bookForm.getName()
                , bookForm.getPrice()
                , bookForm.getStockQuantity()
                , bookForm.getAuthor()
                , bookForm.getIsbn()
        );

        itemService.saveItem(book);
        log.info("save Item compl");

        return "redirect:/items"; //책 목록으로 이동
    }



    //상품 목록 조회

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);

        return "items/itemList";
    }


}
