package com.example.BadmintonShop.Controller;

import com.example.BadmintonShop.DTO.Request.ImageRequest;
import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.DTO.Response.ImageResponse;
import com.example.BadmintonShop.Service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/images") // Endpoint cơ sở cho Image
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /**
     * [READ-ALL] Lấy tất cả các hình ảnh
     * Ai cũng có thể xem (Public)
     */
    @GetMapping
    public ApiResponse<List<ImageResponse>> getAllImages() {
        return ApiResponse.<List<ImageResponse>>builder()
                .code(200)
                .message("Lấy danh sách hình ảnh thành công")
                .data(imageService.getAllImages())
                .build();
    }

    /**
     * [READ-ONE] Lấy một hình ảnh theo ID
     * Ai cũng có thể xem (Public)
     */
    @GetMapping("/{id}")
    public ApiResponse<ImageResponse> getImageById(@PathVariable Integer id) {
        return ApiResponse.<ImageResponse>builder()
                .code(200)
                .message("Lấy hình ảnh thành công")
                .data(imageService.getImageById(id))
                .build();
    }

    /**
     * [CREATE] Tạo hình ảnh mới
     * Chỉ ADMIN mới được phép
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ImageResponse> createImage(@Valid @RequestBody ImageRequest request) {
        return ApiResponse.<ImageResponse>builder()
                .code(201) // 201 Created
                .message("Tạo hình ảnh thành công")
                .data(imageService.createImage(request))
                .build();
    }

    /**
     * [UPDATE] Cập nhật hình ảnh
     * Chỉ ADMIN mới được phép
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ImageResponse> updateImage(@PathVariable Integer id,
                                                  @Valid @RequestBody ImageRequest request) {
        return ApiResponse.<ImageResponse>builder()
                .code(200)
                .message("Cập nhật hình ảnh thành công")
                .data(imageService.updateImage(id, request))
                .build();
    }

    /**
     * [DELETE] Xóa một hình ảnh
     * Chỉ ADMIN mới được phép
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteImage(@PathVariable Integer id) {
        imageService.deleteImage(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa hình ảnh thành công")
                .build();
    }
}