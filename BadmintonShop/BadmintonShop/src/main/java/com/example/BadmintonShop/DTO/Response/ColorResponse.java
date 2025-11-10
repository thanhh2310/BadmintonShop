package com.example.BadmintonShop.DTO.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColorResponse {
    Integer id;
    String name;
}
