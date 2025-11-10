package com.example.BadmintonShop.DTO.Response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class UserProfileResponse {
    private String email;
    private String username;
    private String phone;
    private String address;
    private Date dob;
    private List<String> roles;
}
