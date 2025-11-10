package com.example.BadmintonShop.DTO.Request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class UserUpdateRequest {

    @Size(min = 10, max = 15, message = "Số điện thoại phải từ 10-15 ký tự")
    private String phone;

    private String address;

    private Date dob;
}
