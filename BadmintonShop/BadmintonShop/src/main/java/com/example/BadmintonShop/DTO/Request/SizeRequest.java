package com.example.BadmintonShop.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SizeRequest {

    @NotBlank(message = "Tên size không được để trống")
    private String name;
}