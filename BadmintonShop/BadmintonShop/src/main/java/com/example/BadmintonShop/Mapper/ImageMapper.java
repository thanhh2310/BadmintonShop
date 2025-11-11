package com.example.BadmintonShop.Mapper;

import com.example.BadmintonShop.DTO.Request.ImageRequest;
import com.example.BadmintonShop.DTO.Response.ImageResponse;
import com.example.BadmintonShop.Model.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {

    /**
     * Chuyển từ Entity Image sang DTO ImageResponse
     */
    public ImageResponse toImageResponse(Image image) {
        return ImageResponse.builder()
                .id(image.getId())
                .url(image.getUrl())
                .build();
    }

    /**
     * Chuyển từ DTO ImageRequest sang Entity Image
     */
    public Image toImage(ImageRequest request) {
        return Image.builder()
                .url(request.getUrl())
                .build();
    }
}