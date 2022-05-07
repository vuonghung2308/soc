package com.mh.soc.vo.response;

import com.mh.soc.model.Book;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BookResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final Integer numberOfPage;
    private final String publisher;
    private final String image;
    private final String date;
    private final Float star;
    private final CategoryResponse category;
    private final List<RatingResponse> ratings;

    private static final SimpleDateFormat f =
            new SimpleDateFormat("yyyy-MM-dd");

    public BookResponse(Book b) {
        category = new CategoryResponse(b.getCategory());
        ratings = RatingResponse.get(b.getRating());
        numberOfPage = b.getNumberOfPage();
        description = b.getDescription();
        publisher = b.getPublisher();
        date = f.format(b.getDate());
        image = b.getImage();
        name = b.getName();
        star = b.getStar();
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
