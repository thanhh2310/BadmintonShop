package com.example.BadmintonShop.Controller;

import com.example.BadmintonShop.DTO.Request.SizeRequest;
import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.DTO.Response.SizeResponse;
import com.example.BadmintonShop.Service.SizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sizes") // Endpoint cơ sở cho Size
@RequiredArgsConstructor
public class SizeController {

    private final SizeService sizeService;

    /**
     * [READ-ALL] Lấy tất cả các size
     * Ai cũng có thể xem (Public)
     */
    @GetMapping
    public ApiResponse<List<SizeResponse>> getAllSizes() {
        return ApiResponse.<List<SizeResponse>>builder()
                .code(200)
                .message("Lấy danh sách size thành công")
                .data(sizeService.getAllSizes())
                .build();
    }

    /**
     * [READ-ONE] Lấy một size theo ID
     * Ai cũng có thể xem (Public)
     */
    @GetMapping("/{id}")
    public ApiResponse<SizeResponse> getSizeById(@PathVariable Integer id) {
        return ApiResponse.<SizeResponse>builder()
                .code(200)
                .message("Lấy size thành công")
                .data(sizeService.getSizeById(id))
                .build();
    }

    /**
     * [CREATE] Tạo size mới
     * Chỉ ADMIN mới được phép
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<SizeResponse> createSize(@Valid @RequestBody SizeRequest request) {
        return ApiResponse.<SizeResponse>builder()
                .code(201) // 201 Created
                .message("Tạo size thành công")
                .data(sizeService.createSize(request))
                .build();
    }

    /**
     * [UPDATE] Cập nhật size
     * Chỉ ADMIN mới được phép
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<SizeResponse> updateSize(@PathVariable Integer id,
                                                @Valid @RequestBody SizeRequest request) {
        return ApiResponse.<SizeResponse>builder()
                .code(200)
                .message("Cập nhật size thành công")
                .data(sizeService.updateSize(id, request))
                .build();
    }

    /**
     * [DELETE] Xóa một size
     * Chỉ ADMIN mới được phép
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteSize(@PathVariable Integer id) {
        sizeService.deleteSize(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa size thành công")
                .build();
    }
}