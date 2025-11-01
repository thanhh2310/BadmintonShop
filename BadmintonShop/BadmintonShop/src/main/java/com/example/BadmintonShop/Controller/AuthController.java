package com.example.BadmintonShop.Controller;

import com.example.BadmintonShop.DTO.Request.LoginRequest;
import com.example.BadmintonShop.DTO.Request.RegisterRequest;
import com.example.BadmintonShop.DTO.Request.VerifyRequest;
import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.DTO.Response.TokenResponse;
import com.example.BadmintonShop.Model.User;
import com.example.BadmintonShop.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody RegisterRequest request){
        User user = authService.register(request);
        return ApiResponse.<User>builder()
                .code(200)
                .message("Register successfully! Check email to active account")
                .data(user)
                .build();
    }

    @PostMapping("/verify")
    public ApiResponse<Void> verify(@RequestBody VerifyRequest request){
        authService.verify(request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("active account successfully")
                .build();
    }

    @PostMapping("/login")
    ApiResponse<TokenResponse> login(@RequestBody LoginRequest request){
        return ApiResponse.<TokenResponse>builder()
                .code(200)
                .message("login successfully")
                .data(authService.login(request))
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestHeader("Authorization") String authHeader){
        authService.logout(authHeader);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Logout successfully")
                .build();
    }
}
