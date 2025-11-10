package com.example.BadmintonShop.Controller;

import com.example.BadmintonShop.DTO.Request.BrandRequest;
import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.DTO.Response.BrandResponse;
import com.example.BadmintonShop.Service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/brands") // Endpoint cơ sở
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;


    @GetMapping
    public ApiResponse<List<BrandResponse>> getAllBrands() {
        return ApiResponse.<List<BrandResponse>>builder()
                .code(200)
                .message("Lấy danh sách thương hiệu thành công")
                .data(brandService.getAllBrands())
                .build();
    }


    @GetMapping("/{id}")
    public ApiResponse<BrandResponse> getBrandById(@PathVariable Integer id) {
        return ApiResponse.<BrandResponse>builder()
                .code(200)
                .message("Lấy thương hiệu thành công")
                .data(brandService.getBrandById(id))
                .build();
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BrandResponse> createBrand(@Valid @RequestBody BrandRequest request) {
        return ApiResponse.<BrandResponse>builder()
                .code(201) // 201 Created
                .message("Tạo thương hiệu thành công")
                .data(brandService.createBrand(request))
                .build();
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BrandResponse> updateBrand(@PathVariable Integer id,
                                                  @Valid @RequestBody BrandRequest request) {
        return ApiResponse.<BrandResponse>builder()
                .code(200)
                .message("Cập nhật thương hiệu thành công")
                .data(brandService.updateBrand(id, request))
                .build();
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteBrand(@PathVariable Integer id) {
        brandService.deleteBrand(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa thương hiệu thành công")
                .build();
    }
}