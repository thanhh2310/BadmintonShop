package com.example.BadmintonShop.Controller;

import com.example.BadmintonShop.DTO.Request.ShoesRequest;
import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.DTO.Response.ShoesResponse;
import com.example.BadmintonShop.Service.ShoesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/shoes") // Endpoint cơ sở cho Shoes
@RequiredArgsConstructor
public class ShoesController {

    private final ShoesService shoesService;

    /**
     * [READ-ALL] Lấy tất cả thông số giày
     * Ai cũng có thể xem (Public)
     */
    @GetMapping
    public ApiResponse<List<ShoesResponse>> getAllShoes() {
        return ApiResponse.<List<ShoesResponse>>builder()
                .code(200)
                .message("Lấy danh sách thông số giày thành công")
                .data(shoesService.getAllShoes())
                .build();
    }

    /**
     * [READ-ONE] Lấy thông số giày theo ID
     * Ai cũng có thể xem (Public)
     */
    @GetMapping("/{id}")
    public ApiResponse<ShoesResponse> getShoesById(@PathVariable Integer id) {
        return ApiResponse.<ShoesResponse>builder()
                .code(200)
                .message("Lấy thông số giày thành công")
                .data(shoesService.getShoesById(id))
                .build();
    }

    /**
     * [CREATE] Tạo thông số giày mới
     * Chỉ ADMIN mới được phép
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ShoesResponse> createShoes(@Valid @RequestBody ShoesRequest request) {
        return ApiResponse.<ShoesResponse>builder()
                .code(201) // 201 Created
                .message("Tạo thông số giày thành công")
                .data(shoesService.createShoes(request))
                .build();
    }

    /**
     * [UPDATE] Cập nhật thông số giày
     * Chỉ ADMIN mới được phép
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ShoesResponse> updateShoes(@PathVariable Integer id,
                                                  @Valid @RequestBody ShoesRequest request) {
        return ApiResponse.<ShoesResponse>builder()
                .code(200)
                .message("Cập nhật thông số giày thành công")
                .data(shoesService.updateShoes(id, request))
                .build();
    }

    /**
     * [DELETE] Xóa thông số giày
     * Chỉ ADMIN mới được phép
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteShoes(@PathVariable Integer id) {
        shoesService.deleteShoes(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa thông số giày thành công")
                .build();
    }
}