package com.example.BadmintonShop.Repository;

import com.example.BadmintonShop.Model.Racquet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RacquetRepository extends JpaRepository<Racquet, Integer> {
    // Không có trường unique (như 'name') nên không cần phương thức find... đặc biệt
}