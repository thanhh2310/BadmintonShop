package com.example.BadmintonShop.Controller;

import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.DTO.Response.PaginationResponse;
import com.example.BadmintonShop.DTO.Response.ProductDetailResponseDTO;
import com.example.BadmintonShop.Service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/productDetails")
@RequiredArgsConstructor
public class ProductDetailController {
    private final ProductDetailService productDetailService;

    @GetMapping
    public ApiResponse<PaginationResponse<ProductDetailResponseDTO>> getAllProducts(
            @PageableDefault(size = 12, sort = "id") Pageable pageable
    ){
        return ApiResponse.<PaginationResponse<ProductDetailResponseDTO>>builder()
                .code(200)
                .message("Get all successfully")
                .data(productDetailService.getAllProducts(pageable))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PaginationResponse<ProductDetailResponseDTO>> searchProductsByName(
            @RequestParam("keyword") String keyword,
            @PageableDefault(size = 12, sort = "id") Pageable pageable
    ){
        return ApiResponse.<PaginationResponse<ProductDetailResponseDTO>>builder()
                .code(200)
                .message("Kết quả tìm kiếm cho '" + keyword + "'")
                .data(productDetailService.searchProductsByName(keyword, pageable))
                .build();
    }

    @GetMapping("/filter")
    public ApiResponse<PaginationResponse<ProductDetailResponseDTO>> filterProducts(
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer colorId,
            @RequestParam(required = false) Integer sizeId,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @PageableDefault(size = 12, sort = "id") Pageable pageable) {

        PaginationResponse<ProductDetailResponseDTO> filteredData = productDetailService.filterProducts(
                brandId, categoryId, colorId, sizeId, minPrice, maxPrice, pageable
        );

        return ApiResponse.<PaginationResponse<ProductDetailResponseDTO>>builder()
                .code(200)
                .message("Lọc sản phẩm thành công")
                .data(filteredData)
                .build();
    }
}
