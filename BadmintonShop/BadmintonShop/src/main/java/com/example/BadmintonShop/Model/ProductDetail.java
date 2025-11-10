package com.example.BadmintonShop.Model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@NamedEntityGraph(
        name = "ProductDetail.withDetails",
        attributeNodes = {
                @NamedAttributeNode(value = "product", subgraph = "product-subgraph"),
                @NamedAttributeNode("color"),
                @NamedAttributeNode("size"),
                @NamedAttributeNode("image")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "product-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("brand"),
                                @NamedAttributeNode("category")
                        }
                )
        }
)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String description;
    Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",nullable = false)
    Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id",nullable = false)
    Color color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "size_id",nullable = false)
    Size size;

    Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id",nullable = false)
    Image image;

    @OneToMany(mappedBy = "productDetail", fetch = FetchType.LAZY)
    private Set<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "productDetail", fetch = FetchType.LAZY)
    private Set<CartItem> cartItems;
}
