package com.example.BadmintonShop.Repository;

import com.example.BadmintonShop.Model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    // Cần thiết để kiểm tra URL trùng lặp
    Optional<Image> findByUrl(String url);
}