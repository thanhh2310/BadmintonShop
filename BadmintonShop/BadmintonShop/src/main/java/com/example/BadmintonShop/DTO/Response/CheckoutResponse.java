package com.example.BadmintonShop.DTO.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckoutResponse {
    private Integer orderId;
    private String orderStatus;
    private String paymentUrl; // URL để redirect sang VNPay (nếu có)
    private Integer finalAmount;
}