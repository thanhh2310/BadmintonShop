package com.example.BadmintonShop.Controller;

import com.example.BadmintonShop.DTO.Request.ColorRequest;
import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.DTO.Response.ColorResponse;
import com.example.BadmintonShop.Service.ColorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/colors") // Endpoint cơ sở cho Color
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;

    @GetMapping
    public ApiResponse<List<ColorResponse>> getAllColors() {
        return ApiResponse.<List<ColorResponse>>builder()
                .code(200)
                .message("Lấy danh sách màu sắc thành công")
                .data(colorService.getAllColors())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ColorResponse> getColorById(@PathVariable Integer id) {
        return ApiResponse.<ColorResponse>builder()
                .code(200)
                .message("Lấy màu sắc thành công")
                .data(colorService.getColorById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ColorResponse> createColor(@Valid @RequestBody ColorRequest request) {
        return ApiResponse.<ColorResponse>builder()
                .code(201) // 201 Created
                .message("Tạo màu sắc thành công")
                .data(colorService.createColor(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ColorResponse> updateColor(@PathVariable Integer id,
                                                  @Valid @RequestBody ColorRequest request) {
        return ApiResponse.<ColorResponse>builder()
                .code(200)
                .message("Cập nhật màu sắc thành công")
                .data(colorService.updateColor(id, request))
                .build();
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteColor(@PathVariable Integer id) {
        colorService.deleteColor(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa màu sắc thành công")
                .build();
    }
}