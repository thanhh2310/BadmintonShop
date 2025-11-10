package com.example.BadmintonShop.Repository;

import com.example.BadmintonShop.Model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {
    // Cần thiết để kiểm tra tên trùng lặp
    Optional<Size> findByName(String name);
}