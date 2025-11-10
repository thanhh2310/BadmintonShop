package com.example.BadmintonShop.Service;

import com.example.BadmintonShop.DTO.Request.ChangePasswordRequest;
import com.example.BadmintonShop.DTO.Request.UserUpdateRequest;
import com.example.BadmintonShop.DTO.Response.UserProfileResponse;
import com.example.BadmintonShop.Mapper.UserMapper;
import com.example.BadmintonShop.Model.User;
import com.example.BadmintonShop.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // SỬA LẠI TÌM BẰNG EMAIL

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public UserProfileResponse getMyProfile(User user){
        return userMapper.toUserProfileResponse(user);
    }

    public void updateMyProfile(User user, UserUpdateRequest request) {
        // Cập nhật các trường từ request
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }

        // Lưu lại vào database
        userRepository.save(user);
    }

    public void changePassword(User user, ChangePasswordRequest request) {
        // 1. Kiểm tra mật khẩu cũ có khớp không
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            // Nếu không khớp, ném lỗi (bạn nên dùng Exception tùy chỉnh)
            throw new RuntimeException("Mật khẩu cũ không chính xác");
            // Hoặc: throw new WebErrorConfig(ErrorCode.OLD_PASSWORD_INCORRECT);
        }

        // 2. Mã hóa và đặt mật khẩu mới
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // 3. Lưu lại vào database
        userRepository.save(user);

        // 4. (Nên làm) Vô hiệu hóa tất cả Refresh Token cũ của user này
        //    (Bằng cách xóa chúng khỏi Redis hoặc DB)
    }

    public List<UserProfileResponse> getAllUser(){
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::toUserProfileResponse)
                .collect(Collectors.toList());
    }
}
