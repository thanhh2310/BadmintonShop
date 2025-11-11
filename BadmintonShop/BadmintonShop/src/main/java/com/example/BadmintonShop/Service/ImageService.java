package com.example.BadmintonShop.Service;

import com.example.BadmintonShop.Config.WebErrorConfig;
import com.example.BadmintonShop.DTO.Request.ImageRequest;
import com.example.BadmintonShop.DTO.Response.ImageResponse;
import com.example.BadmintonShop.Enum.ErrorCode;
import com.example.BadmintonShop.Mapper.ImageMapper;
import com.example.BadmintonShop.Model.Image;
import com.example.BadmintonShop.Repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    /**
     * [READ-ALL] Lấy tất cả các hình ảnh
     */
    public List<ImageResponse> getAllImages(){
        List<Image> images = imageRepository.findAll();
        return images.stream()
                .map(imageMapper::toImageResponse)
                .collect(Collectors.toList());
    }

    /**
     * [READ-ONE] Lấy một hình ảnh theo ID
     */
    public ImageResponse getImageById(Integer id){
        Image image = findImageById(id);
        return imageMapper.toImageResponse(image);
    }

    /**
     * [CREATE] Tạo một hình ảnh mới
     */
    public ImageResponse createImage(ImageRequest request){
        // 1. Kiểm tra URL trùng lặp
        if(imageRepository.findByUrl(request.getUrl()).isPresent()){
            throw new WebErrorConfig(ErrorCode.IMAGE_ALREADY_EXISTS);
        }
        // 2. Map sang Entity và lưu
        Image image = imageMapper.toImage(request);
        imageRepository.save(image);

        return imageMapper.toImageResponse(image);
    }

    /**
     * [UPDATE] Cập nhật một hình ảnh
     */
    @Transactional
    public ImageResponse updateImage(Integer id, ImageRequest request){
        // 1. Tìm hình ảnh
        Image image = findImageById(id);

        // 2. Kiểm tra URL mới có bị trùng với hình ảnh *khác* không
        Optional<Image> imageWithNewUrl = imageRepository.findByUrl(request.getUrl());
        if (imageWithNewUrl.isPresent() && !imageWithNewUrl.get().getId().equals(id)) {
            throw new WebErrorConfig(ErrorCode.IMAGE_ALREADY_EXISTS);
        }

        // 3. Cập nhật và lưu
        image.setUrl(request.getUrl());
        imageRepository.save(image);

        return imageMapper.toImageResponse(image);
    }

    /**
     * [DELETE] Xóa một hình ảnh
     */
    @Transactional
    public void deleteImage(Integer id){
        Image existImage = findImageById(id);
        try {
            // 2. Thử xóa
            imageRepository.delete(existImage);
        } catch (DataIntegrityViolationException e) {
            // 3. Bắt lỗi nếu hình ảnh này đang được ProductDetail sử dụng
            throw new WebErrorConfig(ErrorCode.IMAGE_IN_USE);
        }
    }

    // --- Hàm tiện ích (private) ---
    private Image findImageById(Integer id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.IMAGE_NOT_FOUND));
    }
}