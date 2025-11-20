package com.example.BadmintonShop.DTO.Response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CartResponse {
    private Integer cartId;
    private List<CartItemResponse> items;
    private Integer totalItems;      // Tổng số lượng sản phẩm trong giỏ
    private Long totalCartPrice;     // Tổng tiền của cả giỏ hàng
}