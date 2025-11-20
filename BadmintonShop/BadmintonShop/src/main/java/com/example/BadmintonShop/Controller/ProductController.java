package com.example.BadmintonShop.Controller;

import com.example.BadmintonShop.DTO.Request.ProductRequest;
import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.DTO.Response.ProductResponse;
import com.example.BadmintonShop.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .code(200)
                .message("Lấy danh sách sản phẩm thành công")
                .data(productService.getAllProducts())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductResponse(@PathVariable Integer id){
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Get product detail successfully")
                .data(productService.getProductDetail(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .code(201)
                .message("Tạo sản phẩm thành công")
                .data(productService.createProduct(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable Integer id,
                                                      @Valid @RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Cập nhật sản phẩm thành công")
                .data(productService.updateProduct(id, request))
                .build();
    }

    // Admin only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa sản phẩm thành công")
                .build();
    }
}
