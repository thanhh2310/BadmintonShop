package com.example.BadmintonShop.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    @NotBlank(message = "ten san pham khong duoc de trong")
    String name;

    @NotNull(message = "thuong hieu khong duoc de trong")
    Integer brandId;

    @NotNull(message = "Danh muc khong duoc de trong")
    Integer categoryId;

    Integer racquetId;

    Integer shoesId;
}
