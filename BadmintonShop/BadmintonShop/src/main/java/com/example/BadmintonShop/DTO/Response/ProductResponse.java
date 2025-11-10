package com.example.BadmintonShop.DTO.Response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class ProductResponse {
    private Integer productId;
    private String name;
    private String brandName;
    private String categoryName;

    // co hoac khong
    private RacquetResponse racquetResponse;
    private ShoesResponse shoesResponse;

    private Set<ProductDetailResponseDTO> variants;

}
