package com.example.BadmintonShop.Service;

import com.example.BadmintonShop.Config.WebErrorConfig;
import com.example.BadmintonShop.DTO.Request.CheckoutRequest;
import com.example.BadmintonShop.DTO.Response.CheckoutResponse;
import com.example.BadmintonShop.Enum.ErrorCode;
import com.example.BadmintonShop.Model.*;
import com.example.BadmintonShop.Model.OrderDetailId;
import com.example.BadmintonShop.Repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ProductDetailRepository productDetailRepository; // Cần để trừ tồn kho
    private final PaymentService paymentService;

    @Transactional(rollbackFor = Exception.class)
    public CheckoutResponse checkout(String userEmail, CheckoutRequest request, HttpServletRequest httpServletRequest) {

        // 1. Lấy User
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.USER_NOT_FOUND));

        // 2. Lấy Cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống, không thể thanh toán");
        }

        // 3. Tính tổng tiền hàng ban đầu
        int totalProductsPrice = cart.getItems().stream()
                .mapToInt(item -> item.getProductDetail().getPrice() * item.getQuantity())
                .sum();

        int feeShip = 30000;
        int couponDiscount = 0;
        Coupon coupon = null;

        // 4. Xử lý Coupon (Logic Hybrid: <=100 là %, >100 là tiền mặt)
        if (request.getCouponCode() != null && !request.getCouponCode().trim().isEmpty()) {
            coupon =(Coupon) couponRepository.findByCode(request.getCouponCode())
                    .orElseThrow(() -> new RuntimeException("Mã giảm giá không tồn tại"));

            Date now = new Date();

            // --- VALIDATION COUPON ---
            if (coupon.getStartDate() != null && now.before(coupon.getStartDate())) {
                throw new RuntimeException("Mã giảm giá chưa đến đợt áp dụng");
            }
            if (coupon.getExpiryDate() != null && now.after(coupon.getExpiryDate())) {
                throw new RuntimeException("Mã giảm giá đã hết hạn");
            }

            int currentUsage = (coupon.getTimesUsed() == null) ? 0 : coupon.getTimesUsed();
            if (coupon.getUsageLimit() != null && coupon.getUsageLimit() > 0 && currentUsage >= coupon.getUsageLimit()) {
                throw new RuntimeException("Mã giảm giá đã hết lượt sử dụng");
            }

            int minOrder = (coupon.getMinOrderValue() == null) ? 0 : coupon.getMinOrderValue();
            if (totalProductsPrice < minOrder) {
                throw new RuntimeException("Đơn hàng chưa đạt giá trị tối thiểu (" + minOrder + " đ)");
            }

            // --- TÍNH TOÁN (HYBRID) ---
            int discountVal = (coupon.getDiscountType() == null) ? 0 : coupon.getDiscountType();

            if (discountVal <= 100) {
                // Giảm phần trăm (%)
                couponDiscount = (totalProductsPrice * discountVal) / 100;
            } else {
                // Giảm tiền mặt trực tiếp (VND)
                couponDiscount = discountVal;
            }

            // Không được giảm âm tiền
            if (couponDiscount > totalProductsPrice) {
                couponDiscount = totalProductsPrice;
            }

            // Tăng số lần sử dụng coupon
            coupon.setTimesUsed(currentUsage + 1);
            couponRepository.save(coupon);
        }

        int finalTotalAmount = totalProductsPrice + feeShip - couponDiscount;
        if (finalTotalAmount < 0) finalTotalAmount = 0;

        // 5. Lấy Payment Method
        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Phương thức thanh toán không tồn tại"));

        // 6. Tạo Order (Lần 1 để lấy ID)
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setAddress(request.getAddress());
        order.setPaymentMethod(paymentMethod);
        order.setTotalProductsPrice(totalProductsPrice);
        order.setFeeShip(feeShip);
        order.setCouponDiscount(couponDiscount);
        order.setFinalTotalAmount(finalTotalAmount);
        order.setCoupon(coupon);

        if (paymentMethod.getCode().equalsIgnoreCase("VNPAY")) {
            order.setStatus("Pending Payment");
        } else {
            order.setStatus("Processing");
        }

        // Lưu order lần đầu để có ID dùng cho OrderDetail
        Order savedOrder = orderRepository.save(order);

        // 7. Xử lý OrderDetail & Trừ tồn kho
        Set<OrderDetail> orderDetails = new HashSet<>();

        for (CartItem item : cart.getItems()) {
            ProductDetail productDetail = item.getProductDetail();

            // Kiểm tra tồn kho
            if (productDetail.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + productDetail.getProduct().getName()
                        + " không đủ hàng. Còn: " + productDetail.getQuantity());
            }

            // Trừ tồn kho
            productDetail.setQuantity(productDetail.getQuantity() - item.getQuantity());
            productDetailRepository.save(productDetail);

            // Tạo OrderDetail
            OrderDetail detail = new OrderDetail();
            OrderDetailId detailId = new OrderDetailId(savedOrder.getId(), productDetail.getId());

            detail.setId(detailId);
            detail.setOrder(savedOrder);
            detail.setProductDetail(productDetail);
            detail.setQuantity(item.getQuantity());
            detail.setUnitPrice(productDetail.getPrice());
            detail.setDiscountPerItem(0);

            // QUAN TRỌNG: Không gọi orderDetailRepository.save(detail) ở đây!
            // Chỉ add vào Set
            orderDetails.add(detail);
        }

        // Gán danh sách chi tiết vào Order
        savedOrder.setOrderDetails(orderDetails);

        // Lưu lại Order lần 2 -> Cascade sẽ tự động lưu tất cả OrderDetail
        orderRepository.save(savedOrder);

        // 8. Xóa Cart
        cart.getItems().clear();
        cartRepository.save(cart);

        // 9. Thanh toán VNPay (nếu có)
        String paymentUrl = null;
        if (paymentMethod.getCode().equalsIgnoreCase("VNPAY")) {
            paymentUrl = paymentService.createVnPayPayment(
                    savedOrder.getId(),
                    finalTotalAmount,
                    "Thanh toan don hang #" + savedOrder.getId(),
                    httpServletRequest
            );
        }

        return CheckoutResponse.builder()
                .orderId(savedOrder.getId())
                .orderStatus(savedOrder.getStatus())
                .finalAmount(savedOrder.getFinalTotalAmount())
                .paymentUrl(paymentUrl)
                .build();
    } // Rollback nếu có bất kỳ lỗi nào

    @Transactional
    public void updateOrderStatus(Integer orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        order.setStatus(status);
        orderRepository.save(order);
    }
}