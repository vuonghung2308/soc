package com.mh.soc.vo.response;

import com.mh.soc.model.Book;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BookResponse {
    private final Long id;
    private final String name;
    private final String image;
    private final Float star;
    private final Long price;
    private final String author;
    private Integer quantity;

    public BookResponse(Book b) {
        author = b.getAuthor();
        image = b.getImage();
        name = b.getName();
        star = b.getStar();
        price = b.getPrice();
        id = b.getId();
    }

    public BookResponse(Book b, Integer quantity) {
        this.quantity = quantity;
        author = b.getAuthor();
        image = b.getImage();
        name = b.getName();
        star = b.getStar();
        price = b.getPrice();
        id = b.getId();
    }

    public static List<BookResponse> get(List<Book> books) {
        ArrayList<BookResponse> list = new ArrayList<>();
        for (Book book : books) {
            list.add(new BookResponse(book));
        }
        return list;
    }
}
