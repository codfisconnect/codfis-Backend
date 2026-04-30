package com.example.codeFiz.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentsDTO {



    private String name;
    private String gender;
    private String email;
    @Id
    private Long mobile;
    private String courseName;
    private String password;
    private String role;
    private String approvalStatus;
    private String otp;
    private Long otpExpiryTime;

    public StudentsDTO(String name, String gender, String email, Long mobile, String courseName, String approvalStatus) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.courseName = courseName;
        this.approvalStatus = approvalStatus;
        this.role = "STUDENT";
    }

}