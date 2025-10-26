package com.example.BadmintonShop.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendVerificationCode(String toEmail, String code){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Mã xác thực đăng ký tài khoản");
        message.setText("Mã OTP của bạn là: " + code + "\n"
                + "Mã này sẽ hết hạn trong 5 phút.");
        javaMailSender.send(message);
    }
}
