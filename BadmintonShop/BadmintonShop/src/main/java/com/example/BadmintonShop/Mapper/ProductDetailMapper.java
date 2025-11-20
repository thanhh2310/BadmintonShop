package com.example.BadmintonShop.Mapper;

import com.example.BadmintonShop.DTO.Response.ProductDetailResponseDTO;
import com.example.BadmintonShop.Model.ProductDetail;
import org.springframework.stereotype.Component;

@Component
public class ProductDetailMapper {
    public ProductDetailResponseDTO toProductDetailDTO(ProductDetail pd) {
        if (pd == null) {
            return null;
        }

        return ProductDetailResponseDTO.builder()
                .productDetailId(pd.getId())
                .productName(pd.getProduct().getName())
                .brandName(pd.getProduct().getBrand().getName())
                .categoryName(pd.getProduct().getCategory().getName())
                .colorName(pd.getColor().getName())
                .sizeName(pd.getSize().getName())
                .imageUrl(pd.getImage().getUrl())
                .price(pd.getPrice())
                .quantityInStock(pd.getQuantity())
                .description(pd.getDescription())
                .build();
    }
}
