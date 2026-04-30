package com.example.codeFiz.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Codfis Password Reset OTP");
        message.setText(
                "Your OTP for password reset is: " + otp + "\n" +
                        "This OTP is valid for 5 minutes."
        );
        mailSender.send(message);
    }

    public void sendStudentWelcomeMail(
            String toEmail,
            String name,
            String courseName
    ) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("Welcome to Codfis Technologies – Registration Successful");

        String htmlContent =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<body style='margin:0; padding:0; font-family:Arial,sans-serif; background:#f4f6f8;'>" +

                        "<div style='max-width:650px; margin:30px auto; background:#ffffff; border-radius:12px; overflow:hidden; box-shadow:0 6px 18px rgba(0,0,0,0.1);'>" +

                        "<div style='background:linear-gradient(135deg,#0f172a,#1e293b); color:white; padding:20px; text-align:center;'>" +
                        "<h2 style='margin:0;'>Codfis Technologies</h2>" +
                        "<p style='margin:6px 0 0;'>Registration Successful</p>" +
                        "</div>" +

                        "<div style='padding:25px; color:#333; line-height:1.6;'>" +

                        "<p>Dear <strong>" + escapeHtml(name) + "</strong>,</p>" +

                        "<p>Greetings from <strong>Codfis Technologies</strong>!</p>" +

                        "<p>We are pleased to inform you that your registration has been successfully completed.</p>" +

                        "<div style='background:#f8fafc; padding:15px; border-left:4px solid #38bdf8; border-radius:8px; margin:20px 0;'>" +
                        "<h3 style='margin-top:0;'>Registration Details</h3>" +
                        "<p><strong>Name:</strong> " + escapeHtml(name) + "</p>" +
                        "<p><strong>Course:</strong> " + escapeHtml(courseName) + "</p>" +
                        "</div>" +

                        "<p>Our team will contact you shortly with further details regarding the course schedule and onboarding process.</p>" +

                        "<p style='margin-top:25px;'>Warm regards,<br><strong>Codfis Technologies</strong></p>" +

                        "</div>" +

                        getFooter() +

                        "</div>" +
                        "</body>" +
                        "</html>";

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendStudentRegistrationToAdmin(
            String adminEmail,
            String name,
            String gender,
            String email,
            Long mobile,
            String courseName
    ) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(adminEmail);
        helper.setSubject("New Student Registration - Codfis");

        String htmlContent =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<body style='margin:0; padding:0; font-family:Arial,sans-serif; background:#f4f6f8;'>" +

                        "<div style='max-width:650px; margin:30px auto; background:#ffffff; border-radius:12px; overflow:hidden; box-shadow:0 6px 18px rgba(0,0,0,0.1);'>" +

                        "<div style='background:linear-gradient(135deg,#0f172a,#1e293b); color:white; padding:20px; text-align:center;'>" +
                        "<h2 style='margin:0;'>Codfis Technologies</h2>" +
                        "<p style='margin:6px 0 0;'>New Student Registration</p>" +
                        "</div>" +

                        "<div style='padding:25px; color:#333; line-height:1.6;'>" +

                        "<p>A new student has registered.</p>" +

                        "<div style='background:#f8fafc; padding:15px; border-left:4px solid #38bdf8; border-radius:8px; margin:20px 0;'>" +
                        "<h3 style='margin-top:0;'>Student Details</h3>" +
                        "<p><strong>Name:</strong> " + escapeHtml(name) + "</p>" +
                        "<p><strong>Gender:</strong> " + escapeHtml(gender) + "</p>" +
                        "<p><strong>Email:</strong> " + escapeHtml(email) + "</p>" +
                        "<p><strong>Mobile:</strong> " + mobile + "</p>" +
                        "<p><strong>Course:</strong> " + escapeHtml(courseName) + "</p>" +
                        "</div>" +

                        "<p style='margin-top:20px;'>Please review the registration in the admin panel.</p>" +

                        "<p style='margin-top:25px;'>Regards,<br><strong>Codfis System</strong></p>" +

                        "</div>" +

                        getFooter() +

                        "</div>" +
                        "</body>" +
                        "</html>";

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendResultOfTrainerApplication(String toEmail, String name, String status) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);

        String subject;
        String body;

        if ("Approved".equalsIgnoreCase(status)) {
            subject = "Congratulations! You are selected as a Trainer – Codfis Technologies";

            body =
                    "Dear " + name + ",\n\n" +
                            "Greetings from Codfis Technologies!\n\n" +
                            "We are delighted to inform you that your application has been successfully reviewed " +
                            "and you have been selected as a Trainer with Codfis Technologies.\n\n" +
                            "Our team will connect with you shortly regarding onboarding process, " +
                            "and further formalities.\n\n" +
                            "We are excited to have you onboard and looking forward to work together.\n\n" +
                            "Warm regards,\n" +
                            "Codfis Technologies";

        } else if ("Rejected".equalsIgnoreCase(status)) {
            subject = "Codfis Technologies – Trainer Application Update";

            body =
                    "Dear " + name + ",\n\n" +
                            "Greetings from Codfis Technologies!\n\n" +
                            "Thank you for your interest in joining us as a trainer and for taking the time to submit your application.\n\n" +
                            "After careful review, we regret to inform you that we are unable to proceed with your application at this time.\n\n" +
                            "We encourage you to apply for future opportunities that match your profile.\n\n" +
                            "We wish you all the best in your career.\n\n" +
                            "Warm regards,\n" +
                            "Codfis Technologies";
        } else {
            throw new RuntimeException("Invalid status: " + status);
        }

        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendTrainerWelcomeMail(
            String toEmail,
            String name,
            String mobile,
            String description
    ) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("Welcome to Codfis Technologies – Trainer Application Received");

        String htmlContent =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<body style='margin:0; padding:0; font-family:Arial,sans-serif; background:#f4f6f8;'>" +

                        "<div style='max-width:650px; margin:30px auto; background:#ffffff; border-radius:12px; overflow:hidden; box-shadow:0 6px 18px rgba(0,0,0,0.1);'>" +

                        "<div style='background:linear-gradient(135deg,#0f172a,#1e293b); color:white; padding:20px; text-align:center;'>" +
                        "<h2 style='margin:0;'>Codfis Technologies</h2>" +
                        "<p style='margin:6px 0 0;'>Trainer Application Received</p>" +
                        "</div>" +

                        "<div style='padding:25px; color:#333; line-height:1.6;'>" +

                        "<p>Dear <strong>" + escapeHtml(name) + "</strong>,</p>" +
                        "<p>Greetings from <strong>Codfis Technologies</strong>!</p>" +
                        "<p>Thank you for showing interest in joining us as a trainer.</p>" +
                        "<p>We have successfully received your application along with your resume. " +
                        "Our team will review your profile and get back to you shortly.</p>" +

                        "<div style='background:#f8fafc; padding:15px; border-left:4px solid #38bdf8; border-radius:8px; margin:20px 0;'>" +
                        "<h3 style='margin-top:0;'>Application Details</h3>" +
                        "<p><strong>Name:</strong> " + escapeHtml(name) + "</p>" +
                        "<p><strong>Email:</strong> " + escapeHtml(toEmail) + "</p>" +
                        "<p><strong>Mobile:</strong> " + escapeHtml(mobile) + "</p>" +
                        "<p><strong>Description:</strong><br>" + nl2br(description) + "</p>" +
                        "</div>" +

                        "<p>We appreciate your patience during the review process.</p>" +
                        "<p style='margin-top:25px;'>Warm regards,<br><strong>Codfis Technologies</strong></p>" +
                        "</div>" +

                        getFooter() +

                        "</div>" +
                        "</body>" +
                        "</html>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendTrainerApplicationToAdminWithAttachment(
            String adminEmail,
            String name,
            String gender,
            String email,
            String mobile,
            String description,
            String fileName,
            String fileType,
            byte[] resumeBytes
    ) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(adminEmail);
        helper.setSubject("New Trainer Application - Codfis");

        String htmlContent =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<body style='margin:0; padding:0; font-family:Arial,sans-serif; background:#f4f6f8;'>" +

                        "<div style='max-width:650px; margin:30px auto; background:#ffffff; border-radius:12px; overflow:hidden; box-shadow:0 6px 18px rgba(0,0,0,0.1);'>" +

                        "<div style='background:linear-gradient(135deg,#0f172a,#1e293b); color:white; padding:20px; text-align:center;'>" +
                        "<h2 style='margin:0;'>Codfis Technologies</h2>" +
                        "<p style='margin:6px 0 0;'>New Trainer Application</p>" +
                        "</div>" +

                        "<div style='padding:25px; color:#333; line-height:1.6;'>" +
                        "<p>A new trainer has applied.</p>" +

                        "<div style='background:#f8fafc; padding:15px; border-left:4px solid #38bdf8; border-radius:8px; margin:20px 0;'>" +
                        "<p><strong>Name:</strong> " + escapeHtml(name) + "</p>" +
                        "<p><strong>Gender:</strong> " + escapeHtml(gender) + "</p>" +
                        "<p><strong>Email:</strong> " + escapeHtml(email) + "</p>" +
                        "<p><strong>Mobile:</strong> " + escapeHtml(mobile) + "</p>" +
                        "<p><strong>Description:</strong><br>" + nl2br(description) + "</p>" +
                        "<p><strong>Resume:</strong> Attached with this email</p>" +
                        "</div>" +

                        "<p style='margin-top:25px;'>Warm regards,<br><strong>Codfis Technologies</strong></p>" +
                        "</div>" +

                        getFooter() +

                        "</div>" +
                        "</body>" +
                        "</html>";

        helper.setText(htmlContent, true);

        ByteArrayResource resource = new ByteArrayResource(resumeBytes);
        helper.addAttachment(fileName, resource, fileType);

        mailSender.send(message);
    }

    public void sendInterviewMail(
            String toEmail,
            String candidateName,
            String subject,
            String interviewDate,
            String interviewTime,
            String interviewMode,
            String meetingLink,
            String messageText
    ) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject(subject);

        String mapUrl = extractFirstUrl(meetingLink);

        String htmlContent =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<body style='margin:0; padding:0; font-family:Arial,sans-serif; background-color:#f4f6f8;'>" +

                        "<div style='max-width:700px; margin:30px auto; background:#ffffff; border-radius:12px; overflow:hidden; box-shadow:0 6px 18px rgba(0,0,0,0.1);'>" +

                        "<div style='background:linear-gradient(135deg,#0f172a,#1e293b); color:white; padding:24px; text-align:center;'>" +
                        "<h1 style='margin:0; font-size:28px;'>Codfis Technologies</h1>" +
                        "<p style='margin:8px 0 0; font-size:15px;'>Interview Invitation</p>" +
                        "</div>" +

                        "<div style='padding:30px; color:#333; line-height:1.7;'>" +
                        "<p>Dear <strong>" + escapeHtml(candidateName) + "</strong>,</p>" +
                        "<p>Greetings from <strong>Codfis Technologies</strong>!</p>" +
                        "<p>" + nl2br(messageText) + "</p>" +

                        "<div style='margin:24px 0; padding:20px; background:#f8fafc; border-left:5px solid #38bdf8; border-radius:8px;'>" +
                        "<h3 style='margin-top:0; color:#0f172a;'>Interview Details</h3>" +
                        "<p><strong>Date:</strong> " + escapeHtml(interviewDate) + "</p>" +
                        "<p><strong>Time:</strong> " + escapeHtml(interviewTime) + "</p>" +
                        "<p><strong>Mode:</strong> " + escapeHtml(interviewMode) + "</p>" +
                        "<p><strong>Meeting Link / Venue:</strong></p>" +
                        "<div style='background:#f1f5f9; padding:12px; border-radius:6px;'>" +
                        nl2br(meetingLink) +
                        "</div>" +

                        (!"#".equals(mapUrl)
                                ? "<p style='margin-top:15px;'>" +
                                "<a href='" + mapUrl + "' " +
                                "style='display:inline-block; padding:10px 18px; background:#38bdf8; color:white; text-decoration:none; border-radius:6px; font-weight:bold;'>" +
                                "Click Link</a>" +
                                "</p>"
                                : "") +

                        "</div>" +

                        "<p>Kindly be available at the scheduled time. We are looking forward to discuss with you.</p>" +
                        "<p style='margin-top:30px;'>Warm regards,<br><strong>Codfis Technologies</strong></p>" +
                        "</div>" +

                        getFooter() +

                        "</div>" +
                        "</body>" +
                        "</html>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    private String getFooter() {
        return "<div style='background:#e2e8f0; text-align:center; padding:12px; font-size:12px; color:#475569;'>" +
                "This is an official communication from Codfis Technologies" +
                "</div>";
    }

    private String nl2br(String text) {
        if (text == null) {
            return "";
        }
        return escapeHtml(text).replace("\n", "<br>");
    }

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String extractFirstUrl(String text) {
        if (text == null) {
            return "#";
        }
        int index = text.indexOf("http");
        if (index != -1) {
            return text.substring(index).split("\\s")[0];
        }
        return "#";
    }
}