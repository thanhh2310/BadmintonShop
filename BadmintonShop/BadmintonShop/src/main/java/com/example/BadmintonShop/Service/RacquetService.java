package com.example.BadmintonShop.Service;

import com.example.BadmintonShop.Config.WebErrorConfig;
import com.example.BadmintonShop.DTO.Request.RacquetRequest;
import com.example.BadmintonShop.DTO.Response.RacquetResponse;
import com.example.BadmintonShop.Enum.ErrorCode;
import com.example.BadmintonShop.Mapper.RacquetMapper;
import com.example.BadmintonShop.Model.Racquet;
import com.example.BadmintonShop.Repository.RacquetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RacquetService {
    private final RacquetRepository racquetRepository;
    private final RacquetMapper racquetMapper;

    /**
     * [READ-ALL] Lấy tất cả thông số vợt
     */
    public List<RacquetResponse> getAllRacquets() {
        List<Racquet> racquets = racquetRepository.findAll();
        return racquets.stream()
                .map(racquetMapper::toRacquetResponse)
                .collect(Collectors.toList());
    }

    /**
     * [READ-ONE] Lấy thông số vợt theo ID
     */
    public RacquetResponse getRacquetById(Integer id) {
        Racquet racquet = findRacquetById(id);
        return racquetMapper.toRacquetResponse(racquet);
    }

    /**
     * [CREATE] Tạo thông số vợt mới
     */
    public RacquetResponse createRacquet(RacquetRequest request) {
        // Entity này không có trường unique (như name) nên ta bỏ qua bước kiểm tra trùng lặp

        Racquet racquet = racquetMapper.toRacquet(request);
        racquetRepository.save(racquet);

        return racquetMapper.toRacquetResponse(racquet);
    }

    /**
     * [UPDATE] Cập nhật thông số vợt
     */
    @Transactional
    public RacquetResponse updateRacquet(Integer id, RacquetRequest request) {
        // 1. Tìm
        Racquet racquet = findRacquetById(id);

        // 2. Entity này không có trường unique (như name) nên ta bỏ qua bước kiểm tra trùng lặp

        // 3. Cập nhật và lưu
        racquetMapper.updateRacquetFromRequest(racquet, request); // Dùng hàm update
        racquetRepository.save(racquet);

        return racquetMapper.toRacquetResponse(racquet);
    }

    /**
     * [DELETE] Xóa một thông số vợt
     */
    @Transactional
    public void deleteRacquet(Integer id) {
        Racquet existRacquet = findRacquetById(id);
        try {
            // 2. Thử xóa
            racquetRepository.delete(existRacquet);
        } catch (DataIntegrityViolationException e) {
            // 3. Bắt lỗi nếu thông số này đang được Product sử dụng
            throw new WebErrorConfig(ErrorCode.RACQUET_IN_USE);
        }
    }

    // --- Hàm tiện ích (private) ---
    private Racquet findRacquetById(Integer id) {
        return racquetRepository.findById(id)
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.RACQUET_NOT_FOUND));
    }
}