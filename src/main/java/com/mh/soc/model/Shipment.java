package com.mh.soc.model;

import com.mh.soc.vo.request.MakeOrderRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "shipment")
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String address;

    public Shipment(MakeOrderRequest body) {
        this.name = body.getName();
        this.phone = body.getPhone();
        this.address = body.getAddress();
    }
}
