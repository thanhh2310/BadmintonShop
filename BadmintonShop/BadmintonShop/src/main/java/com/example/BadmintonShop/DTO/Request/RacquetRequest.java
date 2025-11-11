package com.example.BadmintonShop.DTO.Request;

import lombok.Data;

@Data
public class RacquetRequest {
    // Chúng ta sẽ thêm tất cả các trường thông số kỹ thuật
    private String length;
    private String rollingLength;
    private String swingWeight;
    private String level;
    private String hardness;
    private String balancePoint;
    private String style;
    private String content;
}