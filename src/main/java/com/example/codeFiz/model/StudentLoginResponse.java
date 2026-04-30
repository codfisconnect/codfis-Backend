package com.example.codeFiz.model;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentLoginResponse {
    private String token;
    private String email;
    private String role;
    private String name;
}
