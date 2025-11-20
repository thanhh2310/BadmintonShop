package com.example.BadmintonShop.Controller;

import com.example.BadmintonShop.DTO.Request.ProductDetailRequest;
import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.DTO.Response.PaginationResponse;
import com.example.BadmintonShop.DTO.Response.ProductDetailResponseDTO;
import com.example.BadmintonShop.Service.ProductDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ApiResponse<ProductDetailResponseDTO> getProductDetailById(@PathVariable Integer id) {
        return ApiResponse.<ProductDetailResponseDTO>builder()
                .code(200)
                .message("Get detail successfully")
                .data(productDetailService.getProductDetailById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductDetailResponseDTO> createProductDetail(@Valid @RequestBody ProductDetailRequest request) {
        return ApiResponse.<ProductDetailResponseDTO>builder()
                .code(201)
                .message("Tạo chi tiết sản phẩm thành công")
                .data(productDetailService.createProductDetail(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductDetailResponseDTO> updateProductDetail(
            @PathVariable Integer id,
            @Valid @RequestBody ProductDetailRequest request) {
        return ApiResponse.<ProductDetailResponseDTO>builder()
                .code(200)
                .message("Cập nhật chi tiết sản phẩm thành công")
                .data(productDetailService.updateProductDetail(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteProductDetail(@PathVariable Integer id) {
        productDetailService.deleteProductDetail(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa chi tiết sản phẩm thành công")
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
