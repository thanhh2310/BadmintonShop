package com.example.BadmintonShop.DTO.Response;

import com.example.BadmintonShop.Model.Shoes;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShoesResponse {
    String form;
    String technology;

    public static ShoesResponse toShoesResponse(Shoes entity) {
        if (entity == null) return null;
        return ShoesResponse.builder()
                .form(entity.getForm())
                .technology(entity.getTechnology())
                .build();
    }
}
