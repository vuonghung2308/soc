package com.mh.soc.controller;



import com.mh.soc.model.Book;
import com.mh.soc.repository.BookRepository;
import com.mh.soc.vo.response.BaseBookResponse;
import com.mh.soc.vo.response.BookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookRepository repository;


    @GetMapping("base")
    public ResponseEntity<?> get() {
        List<Book> list = repository.findAll();

        BaseBookResponse base = new BaseBookResponse();

        list.sort((b1, b2) -> b2.getSold().compareTo(b1.getSold()));
        base.setTopSale(BookResponse.get(list.subList(
                0, Math.min(10, list.size()))));

        list.sort((b1, b2) -> b2.getStar().compareTo(b1.getStar()));
        base.setTopRating(BookResponse.get(list.subList(
                0, Math.min(10, list.size()))));

        list.sort((b1, b2) -> b2.getDate().compareTo(b1.getDate()));
        base.setTopNew(BookResponse.get(list.subList(
                0, Math.min(10, list.size()))));

        list = repository.findByCategory_Id(3L);
        list.sort((b1, b2) -> b2.getStar().compareTo(b1.getStar()));
        base.setTopComic(BookResponse.get(list.subList(
                0, Math.min(10, list.size()))));

        return ResponseEntity.ok(base);
    }

    @GetMapping("")
    public ResponseEntity<?> get(
            @RequestParam Long category
    ) {
        if (category == null) {
            ResponseEntity.ok("ab");
        }
        List<Book> list = repository.findByCategory_Id(category);
        return ResponseEntity.ok(BookResponse.get(list));
    }

    @GetMapping("top-sale")
    public ResponseEntity<?> get(
            @RequestParam(defaultValue = "10", required = false) Integer limit
    ) {
        List<Book> list = repository.findAll();
        list.sort((b1, b2) -> b2.getSold().compareTo(b1.getSold()));
        return ResponseEntity.ok(BookResponse.get(list.subList(
                0, Math.min(limit, list.size()))));
    }

    @GetMapping("search")
    public ResponseEntity<List<Book>> getBookByName(@RequestParam String name) {
        return new ResponseEntity<List<Book>>(repository.searchBookByNameContaining(name), HttpStatus.OK);
    }

//    @GetMapping("123")
//    public ResponseEntity<Book> getBookById(@RequestParam Long id){
//        return new ResponseEntity<Book>(repository.findBookById(id), HttpStatus.OK);
//    }

}