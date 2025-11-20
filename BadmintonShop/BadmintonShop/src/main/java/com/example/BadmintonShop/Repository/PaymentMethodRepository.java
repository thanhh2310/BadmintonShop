package com.example.BadmintonShop.Repository;

import com.example.BadmintonShop.Model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
}
