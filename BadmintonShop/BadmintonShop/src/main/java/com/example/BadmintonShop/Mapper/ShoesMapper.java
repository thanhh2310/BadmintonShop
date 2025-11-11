package com.example.BadmintonShop.Mapper;

import com.example.BadmintonShop.DTO.Request.ShoesRequest;
import com.example.BadmintonShop.DTO.Response.ShoesResponse;
import com.example.BadmintonShop.Model.Shoes;
import org.springframework.stereotype.Component;

@Component
public class ShoesMapper {

    /**
     * Chuyển từ Entity Shoes sang DTO ShoesResponse
     */
    public ShoesResponse toShoesResponse(Shoes shoes) {
        return ShoesResponse.builder()
                .form(shoes.getForm())
                .technology(shoes.getTechnology())
                .build();
    }

    /**
     * Chuyển từ DTO ShoesRequest sang Entity Shoes
     */
    public Shoes toShoes(ShoesRequest request) {
        return Shoes.builder()
                .form(request.getForm())
                .technology(request.getTechnology())
                .build();
    }

    /**
     * Cập nhật Entity từ DTO (dùng cho Update)
     */
    public void updateShoesFromRequest(Shoes shoes, ShoesRequest request) {
        shoes.setForm(request.getForm());
        shoes.setTechnology(request.getTechnology());
    }
}