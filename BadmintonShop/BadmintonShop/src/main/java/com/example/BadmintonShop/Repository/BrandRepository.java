package com.example.BadmintonShop.Repository;

import com.example.BadmintonShop.Model.Brand;
import com.example.BadmintonShop.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    Optional<Brand> findByName(String name);
}
