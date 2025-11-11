package com.example.BadmintonShop.Service;

import com.example.BadmintonShop.Config.WebErrorConfig;
import com.example.BadmintonShop.DTO.Request.ShoesRequest;
import com.example.BadmintonShop.DTO.Response.ShoesResponse;
import com.example.BadmintonShop.Enum.ErrorCode;
import com.example.BadmintonShop.Mapper.ShoesMapper;
import com.example.BadmintonShop.Model.Shoes;
import com.example.BadmintonShop.Repository.ShoesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoesService {
    private final ShoesRepository shoesRepository;
    private final ShoesMapper shoesMapper;

    /**
     * [READ-ALL] Lấy tất cả thông số giày
     */
    public List<ShoesResponse> getAllShoes() {
        List<Shoes> shoes = shoesRepository.findAll();
        return shoes.stream()
                .map(shoesMapper::toShoesResponse)
                .collect(Collectors.toList());
    }

    /**
     * [READ-ONE] Lấy thông số giày theo ID
     */
    public ShoesResponse getShoesById(Integer id) {
        Shoes shoes = findShoesById(id);
        return shoesMapper.toShoesResponse(shoes);
    }

    /**
     * [CREATE] Tạo thông số giày mới
     */
    public ShoesResponse createShoes(ShoesRequest request) {
        // Entity này không có trường unique (như name) nên ta bỏ qua bước kiểm tra trùng lặp

        Shoes shoes = shoesMapper.toShoes(request);
        shoesRepository.save(shoes);

        return shoesMapper.toShoesResponse(shoes);
    }

    /**
     * [UPDATE] Cập nhật thông số giày
     */
    @Transactional
    public ShoesResponse updateShoes(Integer id, ShoesRequest request) {
        // 1. Tìm
        Shoes shoes = findShoesById(id);

        // 2. Entity này không có trường unique (như name) nên ta bỏ qua bước kiểm tra trùng lặp

        // 3. Cập nhật và lưu
        shoesMapper.updateShoesFromRequest(shoes, request); // Dùng hàm update
        shoesRepository.save(shoes);

        return shoesMapper.toShoesResponse(shoes);
    }

    /**
     * [DELETE] Xóa một thông số giày
     */
    @Transactional
    public void deleteShoes(Integer id) {
        Shoes existShoes = findShoesById(id);
        try {
            // 2. Thử xóa
            shoesRepository.delete(existShoes);
        } catch (DataIntegrityViolationException e) {
            // 3. Bắt lỗi nếu thông số này đang được Product sử dụng
            throw new WebErrorConfig(ErrorCode.SHOES_IN_USE);
        }
    }

    // --- Hàm tiện ích (private) ---
    private Shoes findShoesById(Integer id) {
        return shoesRepository.findById(id)
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.SHOES_NOT_FOUND));
    }
}