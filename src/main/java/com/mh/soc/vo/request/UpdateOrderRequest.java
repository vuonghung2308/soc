package com.mh.soc.vo.request;

import com.mh.soc.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequest {
    private Long id;
    private Order.Status status;
}
