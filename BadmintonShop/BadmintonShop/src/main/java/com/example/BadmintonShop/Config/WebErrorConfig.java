package com.example.BadmintonShop.Config;

import com.example.BadmintonShop.Enum.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebErrorConfig extends RuntimeException {
    ErrorCode errorCode;
    public WebErrorConfig(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
