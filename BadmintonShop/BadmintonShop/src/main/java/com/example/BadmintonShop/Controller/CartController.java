package com.example.BadmintonShop.Controller;

import com.example.BadmintonShop.DTO.Request.CartItemRequest;
import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.DTO.Response.CartResponse;
import com.example.BadmintonShop.Service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 1. Lấy giỏ hàng
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<CartResponse> getMyCart(
            // AuthenticationPrincipal trả về Email (String) do JwtFilter của bạn set
            @AuthenticationPrincipal String userEmail) {

        return ApiResponse.<CartResponse>builder()
                .code(200)
                .message("Lấy giỏ hàng thành công")
                .data(cartService.getCurrentCart(userEmail))
                .build();
    }

    // 2. Thêm vào giỏ
    @PostMapping("/items")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<CartResponse> addToCart(
            @AuthenticationPrincipal String userEmail,
            @Valid @RequestBody CartItemRequest request) {

        return ApiResponse.<CartResponse>builder()
                .code(200)
                .message("Thêm vào giỏ hàng thành công")
                .data(cartService.addToCart(userEmail, request))
                .build();
    }

    // 3. Cập nhật số lượng
    @PutMapping("/items/{id}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<CartResponse> updateCartItem(
            @AuthenticationPrincipal String userEmail,
            @PathVariable Integer id,
            @RequestParam Integer quantity) { // Nhận quantity từ query param ?quantity=...

        return ApiResponse.<CartResponse>builder()
                .code(200)
                .message("Cập nhật số lượng thành công")
                .data(cartService.updateCartItemQuantity(userEmail, id, quantity))
                .build();
    }

    // 4. Xóa 1 item
    @DeleteMapping("/items/{id}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<CartResponse> removeCartItem(
            @AuthenticationPrincipal String userEmail,
            @PathVariable Integer id) {

        return ApiResponse.<CartResponse>builder()
                .code(200)
                .message("Xóa sản phẩm khỏi giỏ thành công")
                .data(cartService.removeCartItem(userEmail, id))
                .build();
    }

    // 5. Xóa hết giỏ hàng
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> clearCart(@AuthenticationPrincipal String userEmail) {
        cartService.clearCart(userEmail);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Đã xóa sạch giỏ hàng")
                .build();
    }
}