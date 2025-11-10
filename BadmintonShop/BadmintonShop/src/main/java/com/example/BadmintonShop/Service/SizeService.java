package com.example.BadmintonShop.Service;

import com.example.BadmintonShop.Config.WebErrorConfig;
import com.example.BadmintonShop.DTO.Request.SizeRequest;
import com.example.BadmintonShop.DTO.Response.SizeResponse;
import com.example.BadmintonShop.Enum.ErrorCode;
import com.example.BadmintonShop.Mapper.SizeMapper;
import com.example.BadmintonShop.Model.Size;
import com.example.BadmintonShop.Repository.SizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SizeService {
    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;

    /**
     * [READ-ALL] Lấy tất cả các size
     */
    public List<SizeResponse> getAllSizes(){
        List<Size> sizes = sizeRepository.findAll();
        return sizes.stream()
                .map(sizeMapper::toSizeResponse)
                .collect(Collectors.toList());
    }

    /**
     * [READ-ONE] Lấy một size theo ID
     */
    public SizeResponse getSizeById(Integer id){
        Size size = findSizeById(id);
        return sizeMapper.toSizeResponse(size);
    }

    /**
     * [CREATE] Tạo một size mới
     */
    public SizeResponse createSize(SizeRequest request){
        // 1. Kiểm tra tên trùng lặp
        if(sizeRepository.findByName(request.getName()).isPresent()){
            throw new WebErrorConfig(ErrorCode.SIZE_ALREADY_EXISTS);
        }
        // 2. Map sang Entity và lưu
        Size size = sizeMapper.toSize(request);
        sizeRepository.save(size);

        return sizeMapper.toSizeResponse(size);
    }

    /**
     * [UPDATE] Cập nhật một size
     */
    @Transactional
    public SizeResponse updateSize(Integer id, SizeRequest request){
        // 1. Tìm size
        Size size = findSizeById(id);

        // 2. Kiểm tra tên mới có bị trùng với size *khác* không
        Optional<Size> sizeWithNewName = sizeRepository.findByName(request.getName());
        if (sizeWithNewName.isPresent() && !sizeWithNewName.get().getId().equals(id)) {
            throw new WebErrorConfig(ErrorCode.SIZE_ALREADY_EXISTS);
        }

        // 3. Cập nhật và lưu
        size.setName(request.getName());
        sizeRepository.save(size);

        return sizeMapper.toSizeResponse(size);
    }

    /**
     * [DELETE] Xóa một size
     */
    @Transactional
    public void deleteSize(Integer id){
        Size existSize = findSizeById(id);
        try {
            // 2. Thử xóa
            sizeRepository.delete(existSize);
        } catch (DataIntegrityViolationException e) {
            // 3. Bắt lỗi nếu size này đang được ProductDetail sử dụng
            throw new WebErrorConfig(ErrorCode.SIZE_IN_USE);
        }
    }

    // --- Hàm tiện ích (private) ---
    private Size findSizeById(Integer id) {
        return sizeRepository.findById(id)
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.SIZE_NOT_FOUND));
    }
}