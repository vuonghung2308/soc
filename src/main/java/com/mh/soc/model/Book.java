package com.mh.soc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private Integer numberOfPage;
    private String publisher;
    @ManyToOne
    private Category category;
    @OneToMany
    private List<Rating> rating;
    private Long total;
    private Integer sold;
    private Long price;
    private Date date;
    private String image;
    private String author;

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
