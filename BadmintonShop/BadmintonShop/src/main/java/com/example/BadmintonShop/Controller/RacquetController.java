package com.example.BadmintonShop.Controller;

import com.example.BadmintonShop.DTO.Request.RacquetRequest;
import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.DTO.Response.RacquetResponse;
import com.example.BadmintonShop.Service.RacquetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/racquets") // Endpoint cơ sở cho Racquet
@RequiredArgsConstructor
public class RacquetController {

    private final RacquetService racquetService;

    /**
     * [READ-ALL] Lấy tất cả thông số vợt
     * Ai cũng có thể xem (Public)
     */
    @GetMapping
    public ApiResponse<List<RacquetResponse>> getAllRacquets() {
        return ApiResponse.<List<RacquetResponse>>builder()
                .code(200)
                .message("Lấy danh sách thông số vợt thành công")
                .data(racquetService.getAllRacquets())
                .build();
    }

    /**
     * [READ-ONE] Lấy thông số vợt theo ID
     * Ai cũng có thể xem (Public)
     */
    @GetMapping("/{id}")
    public ApiResponse<RacquetResponse> getRacquetById(@PathVariable Integer id) {
        return ApiResponse.<RacquetResponse>builder()
                .code(200)
                .message("Lấy thông số vợt thành công")
                .data(racquetService.getRacquetById(id))
                .build();
    }

    /**
     * [CREATE] Tạo thông số vợt mới
     * Chỉ ADMIN mới được phép
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<RacquetResponse> createRacquet(@Valid @RequestBody RacquetRequest request) {
        return ApiResponse.<RacquetResponse>builder()
                .code(201) // 201 Created
                .message("Tạo thông số vợt thành công")
                .data(racquetService.createRacquet(request))
                .build();
    }

    /**
     * [UPDATE] Cập nhật thông số vợt
     * Chỉ ADMIN mới được phép
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<RacquetResponse> updateRacquet(@PathVariable Integer id,
                                                      @Valid @RequestBody RacquetRequest request) {
        return ApiResponse.<RacquetResponse>builder()
                .code(200)
                .message("Cập nhật thông số vợt thành công")
                .data(racquetService.updateRacquet(id, request))
                .build();
    }

    /**
     * [DELETE] Xóa thông số vợt
     * Chỉ ADMIN mới được phép
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteRacquet(@PathVariable Integer id) {
        racquetService.deleteRacquet(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa thông số vợt thành công")
                .build();
    }
}