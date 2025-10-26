package com.example.BadmintonShop.Exception;

import com.example.BadmintonShop.Config.WebErrorConfig;
import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.Enum.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = WebErrorConfig.class)
    ResponseEntity<ApiResponse> handlerRunTimeException(WebErrorConfig exception){
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.badRequest()
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}
