package com.mh.soc.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    private Long bookId;
    private Integer quantity;
}
