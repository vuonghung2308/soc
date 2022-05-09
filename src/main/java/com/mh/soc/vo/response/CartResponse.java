package com.mh.soc.vo.response;

import com.mh.soc.model.Book;
import com.mh.soc.model.Cart;
import com.mh.soc.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@AllArgsConstructor
@Setter
public class CartResponse {
    private List<Item> items;
    private Long id;

    public CartResponse(Cart c) {
        id = c.getId();
        items = c.getItems();

    }
}
