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
public class Shoes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String form;
    String technology;

    @OneToMany(mappedBy = "shoes", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Product> products;

}
