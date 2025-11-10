package com.example.BadmintonShop.Mapper;

import com.example.BadmintonShop.DTO.Response.ProductDetailResponseDTO;
import com.example.BadmintonShop.DTO.Response.ProductResponse;
import com.example.BadmintonShop.DTO.Response.RacquetResponse;
import com.example.BadmintonShop.DTO.Response.ShoesResponse;
import com.example.BadmintonShop.Model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ProductDetailMapper productDetailMapper;

    public ProductResponse toProductResponseDTO(Product product) {
        // Chuyển đổi Set<ProductDetail> (Entity) thành List<ProductDetailVariantDTO>
//        List<ProductDetailResponseDTO> variants = product.getProductDetails().stream()
//                .map(pd -> ProductDetailResponseDTO.builder()
//                        .productDetailId(pd.getId())
//                        .description(pd.getDescription())
//                        .price(pd.getPrice())
//                        .quantityInStock(pd.getQuantity())
//                        .colorName(pd.getColor().getName())
//                        .sizeName(pd.getSize().getName())
//                        .imageUrl(pd.getImage().getUrl())
//                        .build())
//                .collect(Collectors.toList());

        Set<ProductDetailResponseDTO> variants = product.getProductDetails().stream()
                .map(productDetailMapper::toProductDetailDTO)
                .collect(Collectors.toSet());

        return ProductResponse.builder()
                .productId(product.getId())
                .name(product.getName())
                .brandName(product.getBrand().getName())
                .categoryName(product.getCategory().getName())
                .racquetResponse(RacquetResponse.toRacquetResponse(product.getRacquet()))
                .shoesResponse(ShoesResponse.toShoesResponse(product.getShoes()))
                .variants(variants)
                .build();
    }
}
