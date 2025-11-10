package com.example.BadmintonShop.Mapper;

import com.example.BadmintonShop.DTO.Request.BrandRequest;
import com.example.BadmintonShop.DTO.Request.ColorRequest;
import com.example.BadmintonShop.DTO.Response.BrandResponse;
import com.example.BadmintonShop.DTO.Response.ColorResponse;
import com.example.BadmintonShop.Model.Brand;
import com.example.BadmintonShop.Model.Color;
import org.springframework.stereotype.Component;

@Component
public class ColorMapper {
    public ColorResponse toColorResponse(Color color){
        return ColorResponse.builder()
                .id(color.getId())
                .name(color.getName())
                .build();
    }

    public Color toColor(ColorRequest request) {
        return Color.builder()
                .name(request.getName())
                .build();
    }
}
