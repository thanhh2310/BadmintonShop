package com.example.BadmintonShop.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyRequest {
    private String email;
    private String otp;
}
