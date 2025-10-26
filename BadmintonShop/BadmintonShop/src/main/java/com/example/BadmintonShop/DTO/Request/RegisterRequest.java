package com.example.BadmintonShop.DTO.Request;

import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
public class RegisterRequest {
    String email;
    String username;
    String password;
    String phone;
    String address;
    Date dob;
}
