package com.example.BadmintonShop.Service;

import com.example.BadmintonShop.Auth.JwtUtils;
import com.example.BadmintonShop.Config.WebErrorConfig;
import com.example.BadmintonShop.DTO.Request.LoginRequest;
import com.example.BadmintonShop.DTO.Request.RegisterRequest;
import com.example.BadmintonShop.DTO.Request.VerifyRequest;
import com.example.BadmintonShop.DTO.Response.TokenResponse;
import com.example.BadmintonShop.Enum.ErrorCode;
import com.example.BadmintonShop.Enum.RoleName;
import com.example.BadmintonShop.Mapper.UserMapper;
import com.example.BadmintonShop.Model.Role;
import com.example.BadmintonShop.Model.User;
import com.example.BadmintonShop.Repository.RoleRepository;
import com.example.BadmintonShop.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final RedisService redisService;

    public User register(RegisterRequest request){
        // Kiểm tra email đã tồn tại và đã active chưa
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent() && existingUser.get().isActive()){
            throw new WebErrorConfig(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user;
        if (existingUser.isPresent()){
            // Nếu user tồn tại nhưng chưa active, ta dùng lại user đó
            user = existingUser.get();
        }else {
            // Nếu user hoàn toàn mới
            user = userMapper.toUser(request);
            user.setActive(false);
        }
        userRepository.save(user);

        //Tạo và lưu OTP
        String code = otpService.generateAndStoreOtp(request.getEmail());

        //Gửi mail
        emailService.sendVerificationCode(request.getEmail(), code);
        return user;
    }

    public void verify(VerifyRequest request){
        // 1. Xác thực OTP
        if(!otpService.validateOtp(request.getEmail(), request.getOtp())){
            throw new WebErrorConfig(ErrorCode.INVALID_OTP_CODE);
        }
        // 2. Kích hoạt tài khoản
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.USER_NOT_FOUND));
        Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER.name());
        user.setActive(true);
        // --- SỬA LẠI CHỖ NÀY ---
        // Tạo một ArrayList (có thể thay đổi được)
        List<Role> roles = new ArrayList<>();
        roles.add(userRole);
        user.setRoles(roles); // Gán list mới
        // --- HẾT PHẦN SỬA ---
        userRepository.save(user);
    }

    public TokenResponse login (LoginRequest request){
        try {
            // 1. Xác thực bằng email (giờ đã hoạt động vì UserService đã sửa)
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            // 2. KHÔNG CẦN query lại DB. Lấy User từ kết quả xác thực.
            User user = (User) auth.getPrincipal();

            // 3. Đảm bảo token của bạn LƯU EMAIL làm "subject"
            String accessToken = jwtUtils.generateAccessToken(user); // Phải dùng user.getEmail() bên trong
            String refreshToken = jwtUtils.generateRefreshToken(user); // Phải dùng user.getEmail() bên trong

            redisService.saveRefreshTokenToRedis(refreshToken,jwtUtils.getJwtLongExpiration());
            return TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (AuthenticationException e) {
            throw new RuntimeException(e); // Nên ném lỗi 401
        }
    }

    public void logout(String authHeader) {
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            // 1. Lấy thời điểm hết hạn tuyệt đối từ token
            Date expirationDate = jwtUtils.extractExpiration(token);
            long expirationMillis = expirationDate.getTime();

            // 2. Lấy thời điểm hiện tại
            long nowMillis = System.currentTimeMillis();

            // 3. Tính toán thời gian còn lại (TTL)
            long ttlMillis = expirationMillis - nowMillis;

            // 4. Chỉ blacklist nếu token chưa hết hạn
            if (ttlMillis > 0) {
                redisService.blackListToken(token, ttlMillis); // Truyền TTL vào
            }
        }
    }
}
