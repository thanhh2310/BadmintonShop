package com.example.BadmintonShop.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailId implements Serializable {
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "productDetail_id")
    private Integer productDetailId;
}
