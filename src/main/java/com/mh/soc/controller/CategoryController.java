package com.mh.soc.controller;

import com.mh.soc.model.Category;
import com.mh.soc.repository.CategoryRepository;
import com.mh.soc.vo.response.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryRepository repository;

    @GetMapping("")
    public ResponseEntity<?> get() {
        List<Category> list = repository.findAll();
        return ResponseEntity.ok(CategoryResponse.get(list));
    }
}
