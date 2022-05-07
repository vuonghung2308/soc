package com.mh.soc.model;

import lombok.Getter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer numberOfPage;
    private String publisher;
    @ManyToOne
    private Category category;
    @ManyToMany
    private List<Rating> rating;
    private Long total;
    private Integer sold;
    private Long price;
    private Date date;
    private String image;

    public Float getStar() {
        if (rating.isEmpty()) {
            return 0F;
        }
        Float result = 0F;
        for (Rating r : rating) {
            result += r.getStar();
        }
        return result / rating.size();
    }
}
