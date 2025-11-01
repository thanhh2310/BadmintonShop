package com.example.BadmintonShop.Model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String url;

    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true)
    Set<ProductDetail> productDetails;
}
