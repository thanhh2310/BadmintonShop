package com.example.BadmintonShop.Repository;

import com.example.BadmintonShop.Model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
}
