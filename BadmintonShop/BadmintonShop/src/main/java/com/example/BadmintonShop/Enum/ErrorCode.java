package com.example.BadmintonShop.Enum;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_ALREADY_EXISTS(401, "User already exists"),
    USER_NOT_FOUND(402, "User not found"),
    INVALID_OTP_CODE(100,"Invalid OTP code" ),
    ROLE_NOT_FOUND(502,"Role not found" ),
    ROLE_ALREADY_EXISTS(501, "Role already exists"),
    CANNOT_DELETE_DEFAULT_ROLE(503, "Can't delete this role"),
    ROLE_IN_USE(504, "Role in use");

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;
    private final String message;


}
