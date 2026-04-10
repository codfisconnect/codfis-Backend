package com.example.codeFiz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    private String courseId;

    private String courseName;
    private String courseDuration;
    private String trainerName;
}