package com.example.codeFiz.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String userId;
    private String password;
}