package com.example.BadmintonShop.DTO.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {
    private Integer cartItemId;
    private Integer quantity;

    // Thông tin sản phẩm để hiển thị lên giỏ hàng
    private Integer productDetailId;
    private String productName;     // Tên chung (Ví dụ: Vợt Yonex)
    private String image;           // Ảnh
    private String color;           // Màu
    private String size;            // Size
    private Integer price;          // Đơn giá
    private Long totalPrice;        // Tổng tiền của item này (price * quantity)
}