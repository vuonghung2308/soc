package com.mh.soc.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    public Rating(User user, Integer star, String comment) {
        this.star = star;
        this.comment = comment;
        this.user = user;
    }
}