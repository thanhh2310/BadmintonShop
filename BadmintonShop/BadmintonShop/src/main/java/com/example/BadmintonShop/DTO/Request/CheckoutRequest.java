package com.example.BadmintonShop.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutRequest {
    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
    private String address;

    @NotNull(message = "Phương thức thanh toán không được để trống")
    private Integer paymentMethodId; // 1: COD, 2: VNPay

    private String couponCode; // Có thể null
    private String note;
}