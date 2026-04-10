package com.example.codeFiz.repository;

import com.example.codeFiz.model.TrainerDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepo extends JpaRepository<TrainerDTO, Long> {
}