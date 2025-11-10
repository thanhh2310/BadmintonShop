package com.example.BadmintonShop.Controller;

import com.example.BadmintonShop.DTO.Request.CategoryRequest;
import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.DTO.Response.CategoryResponse;
import com.example.BadmintonShop.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .code(200)
                .message("Lấy danh sách danh mục thành công")
                .data(categoryService.getAllCategories())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable Integer id) {
        return ApiResponse.<CategoryResponse>builder()
                .code(200)
                .message("Lấy danh mục thành công")
                .data(categoryService.getCategoryById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .code(201) // 201 Created thì chuẩn hơn
                .message("Tạo danh mục thành công")
                .data(categoryService.createCategory(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable Integer id,
                                                        @Valid @RequestBody CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .code(200)
                .message("Cập nhật danh mục thành công")
                .data(categoryService.updateCategory(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa danh mục thành công")
                .build();
    }
}
