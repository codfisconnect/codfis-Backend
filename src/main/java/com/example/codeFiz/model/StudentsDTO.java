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

}