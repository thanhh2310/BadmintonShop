package com.example.BadmintonShop.Mapper;

import com.example.BadmintonShop.DTO.Request.BrandRequest;
import com.example.BadmintonShop.DTO.Request.CategoryRequest;
import com.example.BadmintonShop.DTO.Response.BrandResponse;
import com.example.BadmintonShop.DTO.Response.CategoryResponse;
import com.example.BadmintonShop.Model.Brand;
import com.example.BadmintonShop.Model.Category;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {
    public BrandResponse toBrandResponse(Brand brand){
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }

    public Brand toBrand(BrandRequest request) {
        return Brand.builder()
                .name(request.getName())
                .build();
    }
}
