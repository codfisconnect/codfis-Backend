package com.example.codeFiz.repository;

import com.example.codeFiz.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepo extends JpaRepository<Course, String> {
}