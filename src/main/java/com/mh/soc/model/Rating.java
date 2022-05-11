package com.mh.soc.model;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer star;

    @Column(columnDefinition = "LONGTEXT")
    private String comment;

    @ManyToOne
    private User user;
}