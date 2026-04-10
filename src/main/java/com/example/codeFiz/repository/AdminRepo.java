package com.example.codeFiz.repository;

import com.example.codeFiz.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<AdminUser, String> {
}
