package com.example.BadmintonShop.Mapper;

import com.example.BadmintonShop.DTO.Request.RacquetRequest;
import com.example.BadmintonShop.DTO.Response.RacquetResponse;
import com.example.BadmintonShop.Model.Racquet;
import org.springframework.stereotype.Component;

@Component
public class RacquetMapper {

    /**
     * Chuyển từ Entity Racquet sang DTO RacquetResponse
     */
    public RacquetResponse toRacquetResponse(Racquet racquet) {
        return RacquetResponse.builder()
                .length(racquet.getLength())
                .rollingLength(racquet.getRollingLength())
                .swingWeight(racquet.getSwingWeight())
                .level(racquet.getLevel())
                .hardness(racquet.getHardness())
                .balancePoint(racquet.getBalancePoint())
                .style(racquet.getStyle())
                .content(racquet.getContent())
                .build();
    }

    /**
     * Chuyển từ DTO RacquetRequest sang Entity Racquet
     */
    public Racquet toRacquet(RacquetRequest request) {
        return Racquet.builder()
                .length(request.getLength())
                .rollingLength(request.getRollingLength())
                .swingWeight(request.getSwingWeight())
                .level(request.getLevel())
                .hardness(request.getHardness())
                .balancePoint(request.getBalancePoint())
                .style(request.getStyle())
                .content(request.getContent())
                .build();
    }

    /**
     * Cập nhật Entity từ DTO (dùng cho Update)
     */
    public void updateRacquetFromRequest(Racquet racquet, RacquetRequest request) {
        racquet.setLength(request.getLength());
        racquet.setRollingLength(request.getRollingLength());
        racquet.setSwingWeight(request.getSwingWeight());
        racquet.setLevel(request.getLevel());
        racquet.setHardness(request.getHardness());
        racquet.setBalancePoint(request.getBalancePoint());
        racquet.setStyle(request.getStyle());
        racquet.setContent(request.getContent());
    }
}