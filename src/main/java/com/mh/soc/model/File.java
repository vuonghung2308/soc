package com.mh.soc.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;

    @Lob
    private byte[] data;

    public File(
            String name,
            String type,
            byte[] data
    ) {
        this.name = name;
        this.type = type;
        this.data = data;
    }
}