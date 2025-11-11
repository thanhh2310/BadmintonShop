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
    ROLE_IN_USE(504, "Role in use"),
    PRODUCT_NOT_FOUND(600, "Product not found"),
    CATEGORY_ALREADY_EXISTS(700,"Category already exists" ),
    CATEGORY_IN_USE(701, "Category in use"),
    CATEGORY_NOT_FOUND(702,"Category not found" ),
    BRAND_NOT_FOUND(802,"Brand not found" ),
    BRAND_IN_USE(801,"Brand in use" ),
    BRAND_ALREADY_EXISTS(803,"Brand already exists" ),
    COLOR_NOT_FOUND(901,"Color not found" ),
    COLOR_ALREADY_EXISTS(902,"Color already exists"),
    COLOR_IN_USE(903,"Color in use" ),
    SIZE_ALREADY_EXISTS(1102,"Size already exists" ),
    SIZE_NOT_FOUND(1101,"Size not found" ),
    SIZE_IN_USE(1103,"Size in use" ),
    IMAGE_NOT_FOUND(1201,"Image not found" ),
    IMAGE_ALREADY_EXISTS(1202,"Image already exists" ),
    IMAGE_IN_USE(1203,"Image in use" ),
    RACQUET_NOT_FOUND(1204,"Racquet not found" ),
    RACQUET_IN_USE(1205,"Racquet in use" ),
    SHOES_NOT_FOUND(1206,"Shoes not found" ),
    SHOES_IN_USE(1207,"Shoes in use" );

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;
    private final String message;


}
