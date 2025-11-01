package com.example.BadmintonShop.Model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String code;
    Integer discountType;
    Integer minOrderValue;
    Integer usageLimit;
    Integer timesUsed;
    Date startDate;
    Date expiryDate;

    @OneToMany(mappedBy = "coupon",fetch = FetchType.LAZY)
    Set<Order> orders;
}
