package com.example.BadmintonShop.Mapper;

import com.example.BadmintonShop.DTO.Request.RegisterRequest;
import com.example.BadmintonShop.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User toUser(RegisterRequest request){
        return User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .address(request.getAddress())
                .phone(request.getPhone())
                .dob(request.getDob())
                .build();
    }
}
