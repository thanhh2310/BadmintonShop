package com.example.BadmintonShop.Mapper;

import com.example.BadmintonShop.DTO.Request.CategoryRequest;
import com.example.BadmintonShop.DTO.Response.CategoryResponse;
import com.example.BadmintonShop.Model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryResponse toCategoryResponse(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category toCategory(CategoryRequest request) {
        return Category.builder()
                .name(request.getName())
                .build();
    }
}
