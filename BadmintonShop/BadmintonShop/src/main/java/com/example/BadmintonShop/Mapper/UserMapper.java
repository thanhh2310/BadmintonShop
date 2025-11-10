package com.example.BadmintonShop.Mapper;

import com.example.BadmintonShop.DTO.Request.RegisterRequest;
import com.example.BadmintonShop.DTO.Response.UserProfileResponse;
import com.example.BadmintonShop.Model.Role;
import com.example.BadmintonShop.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public UserProfileResponse toUserProfileResponse(User user){
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());

        return UserProfileResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .phone(user.getPhone())
                .address(user.getAddress())
                .dob(user.getDob())
                .roles(roleNames)
                .build();
    }
}
