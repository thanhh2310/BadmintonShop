package com.example.BadmintonShop.Service;

import com.example.BadmintonShop.Config.WebErrorConfig;
import com.example.BadmintonShop.DTO.Request.ProductRequest;
import com.example.BadmintonShop.DTO.Response.ProductResponse;
import com.example.BadmintonShop.Enum.ErrorCode;
import com.example.BadmintonShop.Mapper.ProductMapper;
import com.example.BadmintonShop.Model.*;
import com.example.BadmintonShop.Repository.*;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final RacquetRepository racquetRepository;
    private final ShoesRepository shoesRepository;
    private final ProductMapper productMapper;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductDetail(Integer id) {
        Product product = productRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toProductResponseDTO(product);
    }

    public ProductResponse createProduct(ProductRequest request) {
        // Kiểm tra tên trùng
        if (productRepository.existsByName(request.getName())) {
            throw new WebErrorConfig(ErrorCode.PRODUCT_ALREADY_EXISTS); // Cần thêm ErrorCode này
        }

        Product product = new Product();
        return saveOrUpdateProduct(product, request);
    }

    @Transactional
    public ProductResponse updateProduct(Integer id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.PRODUCT_NOT_FOUND));

        Optional<Product> productWithSameName = productRepository.findByName(request.getName());

        if (productWithSameName.isPresent() && !productWithSameName.get().getId().equals(id)) {
            throw new WebErrorConfig(ErrorCode.PRODUCT_ALREADY_EXISTS);
        }

        return saveOrUpdateProduct(product, request);
    }

    @Transactional
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new WebErrorConfig(ErrorCode.PRODUCT_NOT_FOUND);
        }
        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new WebErrorConfig(ErrorCode.PRODUCT_IN_USE);
        }
    }

    private ProductResponse saveOrUpdateProduct(Product product, ProductRequest request) {
        product.setName(request.getName());

        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.BRAND_NOT_FOUND));
        product.setBrand(brand);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.CATEGORY_NOT_FOUND));
        product.setCategory(category);

        if (request.getRacquetId() != null) {
            Racquet racquet = racquetRepository.findById(request.getRacquetId())
                    .orElseThrow(() -> new WebErrorConfig(ErrorCode.RACQUET_NOT_FOUND));
            product.setRacquet(racquet);
            product.setShoes(null);
        }

        if (request.getShoesId() != null) {
            Shoes shoes = shoesRepository.findById(request.getShoesId())
                    .orElseThrow(() -> new WebErrorConfig(ErrorCode.SHOES_NOT_FOUND));
            product.setShoes(shoes);
            product.setRacquet(null);
        }

        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponseDTO(savedProduct);
    }
}
