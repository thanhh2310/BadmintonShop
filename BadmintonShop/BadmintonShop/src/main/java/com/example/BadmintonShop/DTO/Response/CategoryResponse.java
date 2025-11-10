package com.example.BadmintonShop.DTO.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {
    Integer id;
    String name;
}
