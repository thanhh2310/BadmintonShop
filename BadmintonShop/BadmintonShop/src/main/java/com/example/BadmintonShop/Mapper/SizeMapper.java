package com.example.BadmintonShop.Mapper;

import com.example.BadmintonShop.DTO.Request.SizeRequest;
import com.example.BadmintonShop.DTO.Response.SizeResponse;
import com.example.BadmintonShop.Model.Size;
import org.springframework.stereotype.Component;

@Component
public class SizeMapper {

    /**
     * Chuyển từ Entity Size sang DTO SizeResponse
     */
    public SizeResponse toSizeResponse(Size size) {
        return SizeResponse.builder()
                .id(size.getId())
                .name(size.getName())
                .build();
    }

    /**
     * Chuyển từ DTO SizeRequest sang Entity Size
     */
    public Size toSize(SizeRequest request) {
        return Size.builder()
                .name(request.getName())
                .build();
    }
}