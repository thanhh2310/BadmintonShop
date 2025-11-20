package com.example.BadmintonShop.Mapper;

import com.example.BadmintonShop.DTO.Response.CartItemResponse;
import com.example.BadmintonShop.DTO.Response.CartResponse;
import com.example.BadmintonShop.Model.Cart;
import com.example.BadmintonShop.Model.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartResponse toCartResponse(Cart cart) {
        // 1. Convert danh sách CartItem -> CartItemResponse
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(this::toCartItemResponse)
                .collect(Collectors.toList());

        // 2. Tính tổng tiền của cả giỏ hàng
        long totalCartPrice = itemResponses.stream()
                .mapToLong(CartItemResponse::getTotalPrice)
                .sum();

        // 3. Tính tổng số lượng sản phẩm
        int totalItems = itemResponses.stream()
                .mapToInt(CartItemResponse::getQuantity)
                .sum();

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(itemResponses)
                .totalItems(totalItems)
                .totalCartPrice(totalCartPrice)
                .build();
    }

    public CartItemResponse toCartItemResponse(CartItem item) {
        long totalPrice = (long) item.getProductDetail().getPrice() * item.getQuantity();

        return CartItemResponse.builder()
                .cartItemId(item.getId())
                .quantity(item.getQuantity())
                .productDetailId(item.getProductDetail().getId())
                // Lấy tên sản phẩm từ Product cha
                .productName(item.getProductDetail().getProduct().getName())
                .image(item.getProductDetail().getImage().getUrl())
                .color(item.getProductDetail().getColor().getName())
                .size(item.getProductDetail().getSize().getName())
                .price(item.getProductDetail().getPrice())
                .totalPrice(totalPrice)
                .build();
    }
}