package com.mh.soc.controller;

import com.mh.soc.interceptor.CustomException;
import com.mh.soc.model.Book;
import com.mh.soc.repository.BookRepository;
import com.mh.soc.vo.response.BaseBookResponse;
import com.mh.soc.vo.response.BookDetailResponse;
import com.mh.soc.vo.response.BookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


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

    @GetMapping("top-sale")
    public ResponseEntity<?> topSale(
            @RequestParam(defaultValue = "100", required = false) Integer limit
    ) {
        List<Book> list = repository.findAll();
        list.sort((b1, b2) -> b2.getSold().compareTo(b1.getSold()));
        return ResponseEntity.ok(BookResponse.get(list.subList(
                0, Math.min(limit, list.size()))));
    }

    @GetMapping("top-new")
    public ResponseEntity<?> topNew(
            @RequestParam(defaultValue = "100", required = false) Integer limit
    ) {
        List<Book> list = repository.findAll();
        list.sort((b1, b2) -> b2.getDate().compareTo(b1.getDate()));
        return ResponseEntity.ok(BookResponse.get(list.subList(
                0, Math.min(limit, list.size()))));
    }

    @GetMapping("")
    public ResponseEntity<?> get(@RequestParam Long id) {
        Optional<Book> optionalBook = repository.findById(id);
        Book book = optionalBook.orElseThrow(() -> new CustomException(
                        "BOOK_NOT_FOUND",
                        "can't find book has id: " + id
                )
        );
        return ResponseEntity.ok(new BookDetailResponse(book));
    }
}