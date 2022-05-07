package com.mh.soc.vo.response;

import com.mh.soc.model.Category;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CategoryResponse {
    private final Long id;
    private final String name;
    private final String icon;

    public CategoryResponse(Category c) {
        id = c.getId();
        name = c.getName();
        icon = c.getIcon();
    }

    public static List<CategoryResponse> get(List<Category> categories) {
        ArrayList<CategoryResponse> list = new ArrayList<>();
        for (Category category : categories) {
            list.add(new CategoryResponse(category));
        }
        return list;
    }
}
