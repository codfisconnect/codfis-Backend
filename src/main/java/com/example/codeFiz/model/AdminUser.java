package com.example.codeFiz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUser {

    @Id
    private String userId;

    private String email;
    private String password;
    private String role;
    private String otp;
    private Long otpExpiryTime;
}