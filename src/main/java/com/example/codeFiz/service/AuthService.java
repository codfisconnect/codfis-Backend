package com.example.codeFiz.service;

import com.example.codeFiz.model.AdminUser;
import com.example.codeFiz.model.LoginRequest;
import com.example.codeFiz.model.LoginResponse;
import com.example.codeFiz.model.RegisterRequest;
import com.example.codeFiz.repository.AdminRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AdminRepo adminRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(AdminRepo adminRepo,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String register(RegisterRequest request) {
        if (adminRepo.existsById(request.getUserId())) {
            return "User ID already exists";
        }

        AdminUser admin = AdminUser.builder()
                .userId(request.getUserId())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ADMIN")
                .build();

        adminRepo.save(admin);
        return "Account created successfully";
    }

    public LoginResponse login(LoginRequest request) {
        AdminUser admin = adminRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Invalid userId or password"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid userId or password");
        }

        String token = jwtService.generateToken(admin.getUserId(), admin.getRole());

        return new LoginResponse(token, admin.getUserId(), admin.getRole());
    }
}