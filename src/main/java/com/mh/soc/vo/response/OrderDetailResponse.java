package com.mh.soc.vo.response;

import com.mh.soc.model.Item;
import com.mh.soc.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderDetailResponse {
    private Long id;
    private String code;
    private Long totalPrice;
    private Integer totalQuantity;
    private String createdTime;
    private Order.Status status;
    private List<BookResponse> items;
    private ShipmentResponse shipment;

    private static final SimpleDateFormat f =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public OrderDetailResponse(Order o) {
        items = new ArrayList<>();
        id = o.getId();
        code = String.valueOf(id + 3000);
        totalQuantity = 0;
        totalPrice = 0L;
        for (Item item : o.getItem()) {
            BookResponse b = new BookResponse(
                    item.getBook(), item.getQuantity());
            totalQuantity += item.getQuantity();
            totalPrice += item.getQuantity() *
                    item.getBook().getPrice();
            items.add(b);
        }
        shipment = new ShipmentResponse(o.getShipment());
        createdTime = f.format(o.getCreatedTime());
        status = o.getStatus();
    }
}
