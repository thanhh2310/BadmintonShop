package com.example.BadmintonShop.DTO.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SizeResponse {
    private Integer id;
    private String name;
}