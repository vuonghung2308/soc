package com.mh.soc.vo.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BaseBookResponse {
    private List<BookResponse> topSale;
    private List<BookResponse> topRating;
    private List<BookResponse> topNew;
    private List<BookResponse> topComic;
}
