package com.example.BadmintonShop.Repository;

import com.example.BadmintonShop.Model.Image;
import com.example.BadmintonShop.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
