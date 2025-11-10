package com.example.BadmintonShop.Service;

import com.example.BadmintonShop.Config.WebErrorConfig;
import com.example.BadmintonShop.DTO.Response.ProductResponse;
import com.example.BadmintonShop.Enum.ErrorCode;
import com.example.BadmintonShop.Mapper.ProductMapper;
import com.example.BadmintonShop.Model.Product;
import com.example.BadmintonShop.Repository.ProductRepository;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public ProductResponse getProductDetail(Integer id){
        Product product =  productRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toProductResponseDTO(product);
    }
}
