package com.example.codeFiz.model;

import lombok.Data;

@Data
public class StudentRegisterRequest {
    private String name;
    private String gender;
    private String email;
    private Long mobile;
    private String courseName;
    private String password;
}
