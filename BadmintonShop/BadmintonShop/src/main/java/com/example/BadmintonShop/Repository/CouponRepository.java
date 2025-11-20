package com.example.BadmintonShop.Repository;

import com.example.BadmintonShop.Model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    Optional<Object> findByCode(String couponCode);
}
