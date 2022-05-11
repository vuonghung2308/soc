package com.mh.soc.vo.response;

import com.mh.soc.model.Shipment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShipmentResponse {
    private String name;
    private String address;
    private String phone;

    public ShipmentResponse(Shipment s) {
        name = s.getName();
        address = s.getAddress();
        phone = s.getPhone();
    }
}
