package com.example.BadmintonShop.Service;

import com.example.BadmintonShop.Config.WebErrorConfig;
import com.example.BadmintonShop.DTO.Request.CategoryRequest;
import com.example.BadmintonShop.DTO.Response.CategoryResponse;
import com.example.BadmintonShop.Enum.ErrorCode;
import com.example.BadmintonShop.Mapper.CategoryMapper;
import com.example.BadmintonShop.Model.Category;
import com.example.BadmintonShop.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(Integer id) {
        Category category = findCategoryById(id);
        return categoryMapper.toCategoryResponse(category);
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        // 1. Kiểm tra tên trùng lặp
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new WebErrorConfig(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        // 2. Chuyển đổi DTO -> Entity
        Category newCategory = categoryMapper.toCategory(request);

        // 3. Lưu vào DB
        Category savedCategory = categoryRepository.save(newCategory);

        // 4. Trả về DTO
        return categoryMapper.toCategoryResponse(savedCategory);
    }

    public CategoryResponse updateCategory(Integer id, CategoryRequest request) {
        // 1. Tìm danh mục hiện tại
        Category existingCategory = findCategoryById(id);

        // 2. Kiểm tra xem tên mới có bị trùng với một danh mục *khác* không
        Optional<Category> categoryWithNewName = categoryRepository.findByName(request.getName());
        if (categoryWithNewName.isPresent() && !categoryWithNewName.get().getId().equals(id)) {
            throw new WebErrorConfig(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        // 3. Cập nhật tên
        existingCategory.setName(request.getName());
        Category updatedCategory = categoryRepository.save(existingCategory);

        // 4. Trả về DTO
        return categoryMapper.toCategoryResponse(updatedCategory);
    }

    public void deleteCategory(Integer id) {
        // 1. Tìm danh mục
        Category categoryToDelete = findCategoryById(id);

        try {
            // 2. Thử xóa
            categoryRepository.delete(categoryToDelete);
        } catch (DataIntegrityViolationException e) {
            // 3. Bắt lỗi nếu danh mục này đang được Product sử dụng
            throw new WebErrorConfig(ErrorCode.CATEGORY_IN_USE);
        }
    }

    // --- Hàm tiện ích (private) ---
    private Category findCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.CATEGORY_NOT_FOUND));
    }

}
