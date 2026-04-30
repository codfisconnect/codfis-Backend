package com.example.codeFiz.service;

import com.example.codeFiz.model.*;
import com.example.codeFiz.model.StudentsDTO;
import com.example.codeFiz.repository.StudentsRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentAuthService {

    private final StudentsRepo studentsRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public StudentAuthService(StudentsRepo studentsRepo,
                              BCryptPasswordEncoder passwordEncoder,
                              JwtService jwtService,
                              EmailService emailService) {
        this.studentsRepo = studentsRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public String register(StudentRegisterRequest request) {

        Optional<StudentsDTO> existingStudentOpt =
                studentsRepo.findByEmailIgnoreCase(request.getEmail().trim());

        if (existingStudentOpt.isPresent()) {
            StudentsDTO existingStudent = existingStudentOpt.get();

            // Already has login account
            if (existingStudent.getPassword() != null && !existingStudent.getPassword().isBlank()) {
                throw new RuntimeException("Student account already exists. Please login.");
            }

            // Convert landing-page student entry into login account
            existingStudent.setPassword(passwordEncoder.encode(request.getPassword()));
            existingStudent.setRole("STUDENT");
            existingStudent.setApprovalStatus("PENDING");
            existingStudent.setOtp(null);
            existingStudent.setOtpExpiryTime(null);

            // update missing/changed fields
            existingStudent.setName(request.getName());
            existingStudent.setGender(request.getGender());
            existingStudent.setMobile(request.getMobile());
            existingStudent.setCourseName(request.getCourseName());

            studentsRepo.save(existingStudent);

            return "Student account request submitted successfully. Wait for admin approval.";
        }

        // New student account
        StudentsDTO student = new StudentsDTO();
        student.setName(request.getName());
        student.setGender(request.getGender());
        student.setEmail(request.getEmail().trim());
        student.setMobile(request.getMobile());
        student.setCourseName(request.getCourseName());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setRole("STUDENT");
        student.setApprovalStatus("PENDING");
        student.setOtp(null);
        student.setOtpExpiryTime(null);

        studentsRepo.save(student);

        return "Student account request submitted successfully. Wait for admin approval.";
    }
    public StudentLoginResponse login(StudentLoginRequest request) {
        StudentsDTO student = studentsRepo.findByEmailIgnoreCase(request.getEmail().trim())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!"APPROVED".equalsIgnoreCase(student.getApprovalStatus())) {
            throw new RuntimeException("Your student account is not approved yet");
        }

        if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(student.getEmail(), "STUDENT");

        return new StudentLoginResponse(
                token,
                student.getEmail(),
                "STUDENT",
                student.getName()
        );
    }
    public String sendStudentOtp(OtpRequest request) {
        StudentsDTO student = studentsRepo.findByEmailIgnoreCase(request.getEmail().trim())
                .orElseThrow(() -> new RuntimeException("Student email not found"));

        if (!"APPROVED".equalsIgnoreCase(student.getApprovalStatus())) {
            throw new RuntimeException("Student account is not approved");
        }

        String otp = String.format("%06d", new java.util.Random().nextInt(999999));
        long expiryTime = System.currentTimeMillis() + (5 * 60 * 1000);

        student.setOtp(otp);
        student.setOtpExpiryTime(expiryTime);
        studentsRepo.save(student);

        emailService.sendOtpEmail(student.getEmail(), otp);

        return "OTP sent successfully";
    }

    public String resetStudentPassword(ResetPasswordRequest request) {
        StudentsDTO student = studentsRepo.findByEmailIgnoreCase(request.getEmail().trim())
                .orElseThrow(() -> new RuntimeException("Student email not found"));

        if (student.getOtp() == null || student.getOtpExpiryTime() == null) {
            throw new RuntimeException("Please request OTP first");
        }

        if (System.currentTimeMillis() > student.getOtpExpiryTime()) {
            throw new RuntimeException("OTP expired");
        }

        if (!student.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        student.setPassword(passwordEncoder.encode(request.getNewPassword()));
        student.setOtp(null);
        student.setOtpExpiryTime(null);

        studentsRepo.save(student);

        return "Student password reset successfully";
    }
    public StudentsDTO getStudentProfile(String email) {
        return studentsRepo.findByEmailIgnoreCase(email.trim())
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }
}