package com.example.codeFiz.repository;

import com.example.codeFiz.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<AdminUser, String> {
    Optional<AdminUser> findByEmailIgnoreCase(String email);}
