package com.example.BadmintonShop.Model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Table(name = "order_detail")
public class OrderDetail {
    @EmbeddedId
    OrderDetailId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productDetailId")
    ProductDetail productDetail;

    Integer quantity;
    Integer unitPrice;
    Integer discountPerItem;
}
