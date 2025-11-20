package com.example.BadmintonShop.Repository;

import com.example.BadmintonShop.Model.Cart;
import com.example.BadmintonShop.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    // Tìm giỏ hàng của một người dùng cụ thể
    Optional<Cart> findByUser(User user);
}