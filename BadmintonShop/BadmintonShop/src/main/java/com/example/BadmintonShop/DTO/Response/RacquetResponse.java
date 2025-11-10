package com.example.BadmintonShop.DTO.Response;

import com.example.BadmintonShop.Model.Racquet;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RacquetResponse {
    String length;
    String rollingLength;
    String swingWeight;
    String level;
    String hardness;
    String balancePoint;
    String style;
    String content;

    public static RacquetResponse toRacquetResponse(Racquet entity) {
        if (entity == null) return null;
        return RacquetResponse.builder()
                .length(entity.getLength())
                .rollingLength(entity.getRollingLength())
                .swingWeight(entity.getSwingWeight())
                .level(entity.getLevel())
                .hardness(entity.getHardness())
                .balancePoint(entity.getBalancePoint())
                .style(entity.getStyle())
                .content(entity.getContent())
                .build();
    }
}
