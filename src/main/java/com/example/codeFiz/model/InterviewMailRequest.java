package com.example.codeFiz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewMailRequest {
    private String toEmail;
    private String candidateName;
    private String subject;
    private String interviewDate;
    private String interviewTime;
    private String interviewMode;
    private String meetingLink;
    private String message;
}
