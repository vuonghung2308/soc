package com.mh.soc.vo.response;

import com.mh.soc.model.Cart;
import com.mh.soc.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
public class CartResponse {
    private Long id;
    private Long totalPrice;
    private Integer totalQuantity;
    private List<BookResponse> items;

    public CartResponse(Cart c) {
        items = new ArrayList<>();
        totalPrice = 0L;
        totalQuantity = 0;
        for (Item item : c.getItem()) {
            BookResponse b = new BookResponse(
                    item.getBook(), item.getQuantity());
            totalQuantity += item.getQuantity();
            totalPrice += item.getQuantity() *
                    item.getBook().getPrice();
            items.add(b);
        }
        id = c.getId();
    }
}
