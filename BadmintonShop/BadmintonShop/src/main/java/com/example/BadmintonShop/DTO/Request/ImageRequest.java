package com.example.BadmintonShop.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL; // Import để validation URL
import lombok.Data;

@Data
public class ImageRequest {

    @NotBlank(message = "URL hình ảnh không được để trống")
    @URL(message = "Định dạng URL không hợp lệ") // Đảm bảo đây là một URL
    private String url;
}