package com.mh.soc.repository;

import com.mh.soc.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByCategory_Id(Long category);
}
