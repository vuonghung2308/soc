package com.mh.soc.model;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Item> item;
    @OneToOne
    private Shipment shipment;
    private Date createdTime;

    @Enumerated(EnumType.STRING)
    private Status status;


    public enum Status {
        INITIATED, CONFIRMED, SHIPPED
    }

    public Order(Cart cart, Shipment shipment) {
        item = new ArrayList<>(cart.getItem());
        createdTime = new Date();
        status = Status.INITIATED;
        this.shipment = shipment;
    }
}
