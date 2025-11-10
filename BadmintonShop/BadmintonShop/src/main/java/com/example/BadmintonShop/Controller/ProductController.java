package com.example.BadmintonShop.Controller;

import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.DTO.Response.ProductResponse;
import com.example.BadmintonShop.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductResponse(@PathVariable Integer id){
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Get product detail successfully")
                .data(productService.getProductDetail(id))
                .build();
    }
}
