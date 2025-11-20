package com.example.BadmintonShop.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDetailRequest {

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    private Integer price;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0")
    private Integer quantity;

    // ID của các đối tượng liên quan
    @NotNull(message = "Phải chọn sản phẩm")
    private Integer productId;

    @NotNull(message = "Phải chọn màu sắc")
    private Integer colorId;

    @NotNull(message = "Phải chọn kích cỡ")
    private Integer sizeId;

    @NotNull(message = "Phải chọn hình ảnh")
    private Integer imageId;
}