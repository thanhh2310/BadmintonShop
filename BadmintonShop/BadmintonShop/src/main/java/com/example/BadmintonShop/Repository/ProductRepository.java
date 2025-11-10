package com.example.BadmintonShop.Repository;

import com.example.BadmintonShop.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN FETCH p.brand " +
            "LEFT JOIN FETCH p.category " +
            "LEFT JOIN FETCH p.racquet " +
            "LEFT JOIN FETCH p.shoes " +
            "LEFT JOIN FETCH p.productDetails pd " +
            "LEFT JOIN FETCH pd.color " +
            "LEFT JOIN FETCH pd.size " +
            "LEFT JOIN FETCH pd.image " +
            "WHERE p.id = :id")
    Optional<Product> findByIdWithDetails(@Param("id") Integer id);
}
