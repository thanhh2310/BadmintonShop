package com.example.BadmintonShop.Repository;

import com.example.BadmintonShop.Model.ProductDetail;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductDetailRepository extends
        JpaRepository<ProductDetail, Integer>,
        JpaSpecificationExecutor<ProductDetail> {
    @Query(value = "SELECT pd FROM ProductDetail pd " +
            "JOIN FETCH pd.product p " +
            "JOIN FETCH p.brand " +
            "JOIN FETCH p.category " +
            "JOIN FETCH pd.color " +
            "JOIN FETCH pd.size " +
            "JOIN FETCH pd.image",
            countQuery = "SELECT COUNT(pd) FROM ProductDetail pd")
    Page<ProductDetail> findAllWithDetail(Pageable pageable);

    @Query(value = "SELECT pd FROM ProductDetail pd " +
            "JOIN FETCH pd.product p " +
            "JOIN FETCH p.brand " +
            "JOIN FETCH p.category " +
            "JOIN FETCH pd.color " +
            "JOIN FETCH pd.size " +
            "JOIN FETCH pd.image " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))", // Điều kiện tìm kiếm
            countQuery = "SELECT COUNT(pd) FROM ProductDetail pd " +
                    "JOIN pd.product p " +
                    "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ProductDetail> findByProductNameContaining(
            @Param("keyword") String keyword,
            Pageable pageable);

    @Override
    @EntityGraph(value = "ProductDetail.withDetails") // <-- ÁP DỤNG ENTITY GRAPH
    Page<ProductDetail> findAll(Specification<ProductDetail> spec, Pageable pageable);
}
