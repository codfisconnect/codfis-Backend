package com.example.codeFiz.repository;

import com.example.codeFiz.model.StudentsDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentsRepo extends JpaRepository<StudentsDTO, Long> {
    List<StudentsDTO> findByCourseNameContainingIgnoreCase(String courseName);
    Optional<StudentsDTO> findByEmailIgnoreCase(String email);
}