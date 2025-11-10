package com.example.BadmintonShop.DTO.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDetailResponseDTO {
    private Integer productDetailId;
    private String productName;
    private String brandName;
    private String categoryName;
    private String colorName;
    private String sizeName;
    private String imageUrl;
    private Integer price;
    private Integer quantityInStock;
    private String description;
}
