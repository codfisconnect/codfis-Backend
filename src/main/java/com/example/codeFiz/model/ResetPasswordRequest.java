package com.example.codeFiz.model;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String userId;
    private String otp;
    private String newPassword;
}