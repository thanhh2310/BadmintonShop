package com.example.BadmintonShop.Controller;

import com.example.BadmintonShop.DTO.Request.ChangePasswordRequest;
import com.example.BadmintonShop.DTO.Request.UserUpdateRequest;
import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.DTO.Response.UserProfileResponse;
import com.example.BadmintonShop.Model.User;
import com.example.BadmintonShop.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users") // Đường dẫn cơ sở cho các API liên quan đến User
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getMyProfile(){

        return ApiResponse.<UserProfileResponse>builder()
                .code(200)
                .message("Get successfully")
                .data(userService.getMyProfile())
                .build();
    }

    @PutMapping("/me")
    public ApiResponse<Void> updateMyProfile (@AuthenticationPrincipal String email,
                                              @Valid @RequestBody UserUpdateRequest request){
        userService.updateMyProfile(email, request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update successfully")
                .build();
    }

    @PostMapping("/me/change-password")
    public ApiResponse<Void> changePassword(@AuthenticationPrincipal String email,
                                            @Valid @RequestBody ChangePasswordRequest request){
        userService.changePassword(email, request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Change password successfully")
                .build();
    }

    @GetMapping()
    public ApiResponse<List<UserProfileResponse>> getAllUser(){
        return ApiResponse.<List<UserProfileResponse>>builder()
                .code(200)
                .message("Get all users successfully")
                .data(userService.getAllUser())
                .build();
    }
}
