package com.example.codeFiz.controller;

import com.example.codeFiz.model.OtpRequest;
import com.example.codeFiz.model.ResetPasswordRequest;
import com.example.codeFiz.model.StudentLoginRequest;
import com.example.codeFiz.model.StudentLoginResponse;
import com.example.codeFiz.model.StudentRegisterRequest;
import com.example.codeFiz.model.StudentsDTO;
import com.example.codeFiz.service.StudentAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student-auth")
@CrossOrigin(origins = "*")
public class StudentAuthController {

    private final StudentAuthService studentAuthService;

    public StudentAuthController(StudentAuthService studentAuthService) {
        this.studentAuthService = studentAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody StudentRegisterRequest request) {
        try {
            return ResponseEntity.ok(studentAuthService.register(request));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody StudentLoginRequest request) {
        try {
            return ResponseEntity.ok(studentAuthService.login(request));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<StudentsDTO> getProfile(@PathVariable String email) {
        return ResponseEntity.ok(studentAuthService.getStudentProfile(email));
    }
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendStudentOtp(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(studentAuthService.sendStudentOtp(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetStudentPassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(studentAuthService.resetStudentPassword(request));
    }
}