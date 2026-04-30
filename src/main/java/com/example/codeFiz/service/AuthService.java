package com.example.codeFiz.service;

import com.example.codeFiz.model.AdminUser;
import com.example.codeFiz.model.LoginRequest;
import com.example.codeFiz.model.LoginResponse;
import com.example.codeFiz.model.OtpRequest;
import com.example.codeFiz.model.RegisterRequest;
import com.example.codeFiz.model.ResetPasswordRequest;
import com.example.codeFiz.repository.AdminRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    private final AdminRepo adminRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthService(AdminRepo adminRepo,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       EmailService emailService) {
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public String register(RegisterRequest request) {
        if (adminRepo.existsById(request.getUserId())) {
            throw new RuntimeException("User ID already exists");
        }

        AdminUser user = AdminUser.builder()
                .userId(request.getUserId())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ADMIN")
                .build();

        adminRepo.save(user);
        return "Account created successfully";
    }

    public LoginResponse login(LoginRequest request) {
        AdminUser user = adminRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Invalid userId or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid userId or password");
        }

        String token = jwtService.generateToken(user.getUserId(), user.getRole());

        return new LoginResponse(token, user.getUserId(), user.getRole());
    }

    public String sendOtp(OtpRequest request) {
        String email = request.getEmail().trim();

        AdminUser user = adminRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        String otp = String.format("%06d", new Random().nextInt(999999));
        long expiryTime = System.currentTimeMillis() + (5 * 60 * 1000);

        try {
            emailService.sendOtpEmail(user.getEmail(), otp);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email");
        }

        user.setOtp(otp);
        user.setOtpExpiryTime(expiryTime);
        adminRepo.save(user);

        return "OTP sent successfully";
    }



    public String resetPassword(ResetPasswordRequest request) {
        Optional<AdminUser> optionalUser = adminRepo.findByEmailIgnoreCase(request.getEmail());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Email not found");
        }

        AdminUser user = optionalUser.get();

        if (user.getOtp() == null || user.getOtpExpiryTime() == null) {
            throw new RuntimeException("Please request OTP first");
        }

        if (System.currentTimeMillis() > user.getOtpExpiryTime()) {
            throw new RuntimeException("OTP expired");
        }

        if (!user.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOtp(null);
        user.setOtpExpiryTime(null);

        adminRepo.save(user);

        return "Password reset successfully";
    }
}