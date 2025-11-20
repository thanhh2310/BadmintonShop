package com.example.BadmintonShop.Controller;

import com.example.BadmintonShop.DTO.Request.CheckoutRequest;
import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.DTO.Response.CheckoutResponse;
import com.example.BadmintonShop.Service.OrderService;
import com.example.BadmintonShop.Service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class CheckoutController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    @PostMapping("/place-order")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<CheckoutResponse> placeOrder(
            @AuthenticationPrincipal String userEmail,
            @Valid @RequestBody CheckoutRequest request,
            HttpServletRequest httpServletRequest // Cần để lấy IP cho VNPay
    ) {
        CheckoutResponse response = orderService.checkout(userEmail, request, httpServletRequest);

        return ApiResponse.<CheckoutResponse>builder()
                .code(200)
                .message("Đặt hàng thành công")
                .data(response)
                .build();
    }

    @GetMapping("/vnpay-return")
    public ApiResponse<String> vnpayReturn(@RequestParam Map<String, String> allParams) {
        // 1. Kiểm tra chữ ký (Checksum) để đảm bảo dữ liệu không bị giả mạo
        boolean isVerified = paymentService.verifyPayment(allParams);

        if (!isVerified) {
            return ApiResponse.<String>builder()
                    .code(400)
                    .message("Lỗi xác thực: Chữ ký không hợp lệ!")
                    .build();
        }

        // 2. Lấy thông tin giao dịch
        String vnp_ResponseCode = allParams.get("vnp_ResponseCode");
        String orderIdStr = allParams.get("vnp_TxnRef"); // Đây chính là orderId mình đã gửi đi
        Integer orderId = Integer.parseInt(orderIdStr);

        // 3. Xử lý kết quả
        if ("00".equals(vnp_ResponseCode)) {
            // Giao dịch thành công
            orderService.updateOrderStatus(orderId, "Paid"); // Hoặc "Đã thanh toán"

            return ApiResponse.<String>builder()
                    .code(200)
                    .message("Thanh toán thành công! Đơn hàng #" + orderId + " đã được xác nhận.")
                    .build();
        } else {
            // Giao dịch thất bại / Hủy bỏ
            orderService.updateOrderStatus(orderId, "Payment Failed"); // Hoặc "Thanh toán thất bại"

            return ApiResponse.<String>builder()
                    .code(402) // Payment Required hoặc lỗi tùy chọn
                    .message("Thanh toán thất bại hoặc bị hủy bỏ. Mã lỗi: " + vnp_ResponseCode)
                    .build();
        }
    }
}