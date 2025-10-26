package com.example.BadmintonShop.DTO.Response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
