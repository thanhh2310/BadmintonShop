package com.example.BadmintonShop.Service;

import com.example.BadmintonShop.Config.WebErrorConfig;
import com.example.BadmintonShop.DTO.Request.CartItemRequest;
import com.example.BadmintonShop.DTO.Response.CartResponse;
import com.example.BadmintonShop.Enum.ErrorCode;
import com.example.BadmintonShop.Mapper.CartMapper;
import com.example.BadmintonShop.Model.*;
import com.example.BadmintonShop.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductDetailRepository productDetailRepository;
    private final UserRepository userRepository; // Cần để tìm user nếu chưa có cart
    private final CartMapper cartMapper;


    public Cart getOrCreateCart(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.USER_NOT_FOUND)); // Nên dùng ErrorCode chuẩn

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setItems(new HashSet<>());
                    newCart.setActive(true);
                    newCart.setCreateAt(new Date());
                    newCart.setUpdateAt(new Date());
                    return cartRepository.save(newCart);
                });
    }

    /**
     * 1. GET /cart: Lấy giỏ hàng hiện tại
     */
    public CartResponse getCurrentCart(String userEmail) {
        Cart cart = getOrCreateCart(userEmail);
        return cartMapper.toCartResponse(cart);
    }

    /**
     * 2. POST /cart/items: Thêm sản phẩm vào giỏ
     */
    @Transactional
    public CartResponse addToCart(String userEmail, CartItemRequest request) {
        Cart cart = getOrCreateCart(userEmail);
        cart.setUpdateAt(new Date()); // Cập nhật thời gian sửa

        // Kiểm tra sản phẩm có tồn tại không
        ProductDetail productDetail = productDetailRepository.findById(request.getProductDetailId())
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        // Kiểm tra số lượng tồn kho
        if (productDetail.getQuantity() < request.getQuantity()) {
            throw new WebErrorConfig(ErrorCode.OUT_OF_STOCK); // Nên tạo ErrorCode: OUT_OF_STOCK
        }

        // Kiểm tra xem sản phẩm này ĐÃ CÓ trong giỏ hàng chưa
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductDetail().getId().equals(request.getProductDetailId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Nếu có rồi -> Cộng dồn số lượng
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            // Nếu chưa có -> Tạo CartItem mới
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductDetail(productDetail);
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);

            // Thêm vào set của cart để trả về dữ liệu mới nhất
            cart.getItems().add(newItem);
        }

        // Lưu cart (để cập nhật updateAt)
        cartRepository.save(cart);

        return cartMapper.toCartResponse(cart);
    }

    /**
     * 3. PUT /cart/items/{id}: Cập nhật số lượng item
     */
    @Transactional
    public CartResponse updateCartItemQuantity(String userEmail, Integer cartItemId, Integer newQuantity) {
        Cart cart = getOrCreateCart(userEmail);
        cart.setUpdateAt(new Date());

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.CART_ITEM_NOT_FOUND)); // ErrorCode: CART_ITEM_NOT_FOUND

        // Bảo mật: Đảm bảo item này thuộc về giỏ hàng của user đang đăng nhập
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new WebErrorConfig(ErrorCode.FORBIDDEN); // ErrorCode: FORBIDDEN
        }

        if (newQuantity <= 0) {
            // Nếu số lượng <= 0 thì xóa luôn item đó
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            // Cập nhật số lượng
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        }

        cartRepository.save(cart);
        return cartMapper.toCartResponse(cart);
    }

    /**
     * 4. DELETE /cart/items/{id}: Xóa 1 item khỏi giỏ
     */
    @Transactional
    public CartResponse removeCartItem(String userEmail, Integer cartItemId) {
        Cart cart = getOrCreateCart(userEmail);
        cart.setUpdateAt(new Date());

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.CART_ITEM_NOT_FOUND));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new WebErrorConfig(ErrorCode.FORBIDDEN);
        }

        // Xóa khỏi set của cart trước (để Hibernate đồng bộ)
        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        cartRepository.save(cart);

        return cartMapper.toCartResponse(cart);
    }

    /**
     * 5. DELETE /cart: Xóa sạch giỏ hàng
     */
    @Transactional
    public void clearCart(String userEmail) {
        Cart cart = getOrCreateCart(userEmail);
        cart.getItems().clear();
        cart.setUpdateAt(new Date());

        cartRepository.save(cart);
    }
}