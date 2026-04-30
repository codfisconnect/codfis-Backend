package com.example.codeFiz.service;

import com.example.codeFiz.model.Course;
import com.example.codeFiz.model.StudentsDTO;
import com.example.codeFiz.model.TrainerDTO;
import com.example.codeFiz.repository.CourseRepo;
import com.example.codeFiz.repository.StudentsRepo;
import com.example.codeFiz.repository.TrainerRepo;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CourseService {

    @Autowired
     CourseRepo courseRepo;

    @Autowired
    StudentsRepo studentsRepo;

    @Autowired
    TrainerRepo trainerRepo;

    @Autowired
    EmailService emailService;

    @Value("${codfis.admin.email}")
    private String adminEmail;


    public CourseService(CourseRepo courseRepo, StudentsRepo studentsRepo, TrainerRepo trainerRepo) {
        this.courseRepo = courseRepo;
        this.studentsRepo = studentsRepo;
        this.trainerRepo = trainerRepo;
    }

    // =========================
    // COURSE METHODS
    // =========================

    public List<Course> availableCourses() {
        return courseRepo.findAll();
    }

    public Course addCourse(Course course) {
        if (courseRepo.existsById(course.getCourseId())) {
            throw new RuntimeException("Course ID already exists: " + course.getCourseId());
        }
        return courseRepo.save(course);
    }

    public Course updateCourse(String id, Course updatedCourse) {

        Course existingCourse = courseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        existingCourse.setCourseName(updatedCourse.getCourseName());
        existingCourse.setCourseDuration(updatedCourse.getCourseDuration());
        existingCourse.setTrainerName(updatedCourse.getTrainerName());

        return courseRepo.save(existingCourse);
    }

    public void deleteCourse(String id) {
        if (!courseRepo.existsById(id)) {
            throw new RuntimeException("Course not found with id: " + id);
        }
        courseRepo.deleteById(id);
    }

    // =========================
    // STUDENT METHODS
    // =========================

    public List<StudentsDTO> enrolledStudents() {
        return studentsRepo.findAll();
    }

    public List<StudentsDTO> searchStudentbycourse(String courseName) {
        if (courseName == null || courseName.isBlank()) {
            return studentsRepo.findAll();
        }
        return studentsRepo.findByCourseNameContainingIgnoreCase(courseName);
    }

    public void enrollStudents(String name,
                                      String gender,
                                      String email,
                                      Long mobile,
                                      String courseName) {
        StudentsDTO student = StudentsDTO.builder()
                .name(name)
                .gender(gender)
                .email(email)
                .mobile(mobile)
                .courseName(courseName)
                .build();
         studentsRepo.save(student);

         try {
             emailService.sendStudentRegistrationToAdmin(
                     adminEmail,
                     name,
                     gender,
                     email,
                     mobile,
                     courseName
             );

             emailService.sendStudentWelcomeMail(
                     email,
                     name,
                     courseName

             );
         }catch (Exception e){
             e.printStackTrace();
         }
    }

    public StudentsDTO updateStudent(Long mobile, StudentsDTO updatedStudent) {
        StudentsDTO existingStudent = studentsRepo.findById(mobile)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + mobile));

        existingStudent.setName(updatedStudent.getName());
        existingStudent.setGender(updatedStudent.getGender());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setMobile(updatedStudent.getMobile());
        existingStudent.setCourseName(updatedStudent.getCourseName());

        return studentsRepo.save(existingStudent);
    }

    public void deleteStudent(Long mobile) {
        if (!studentsRepo.existsById(mobile)) {
            throw new RuntimeException("Student not found with id: " + mobile);
        }
        studentsRepo.deleteById(mobile);
    }

    // =========================
    // TRAINER METHODS
    // =========================

    public void trainerRegister(String name,
                                String gender,
                                String email,
                                String mobile,
                                String description,
                                MultipartFile file) throws IOException {

        TrainerDTO trainer = new TrainerDTO();
        trainer.setName(name);
        trainer.setGender(gender);
        trainer.setEmail(email);
        trainer.setMobile(mobile);
        trainer.setDescription(description);
        trainer.setResume(file.getBytes());
        trainer.setFileName(file.getOriginalFilename());
        trainer.setFileType(file.getContentType());

        // optional if your TrainerDTO has status field
        // trainer.setStatus("PENDING");

        trainerRepo.save(trainer);

        try {
            emailService.sendTrainerApplicationToAdminWithAttachment(
                    adminEmail,
                    name,
                    gender,
                    email,
                    mobile,
                    description,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            emailService.sendTrainerWelcomeMail(
                    email,
                    name,
                    mobile,
                    description
            );
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }

    public TrainerDTO getTrainerByMobile(Long mobile) {
        return trainerRepo.findById(mobile)
                .orElseThrow(() -> new RuntimeException("Trainer not found with mobile: " + mobile));
    }

    public TrainerDTO getTrainerResumeByMobile(Long mobile) {
        return trainerRepo.findById(mobile)
                .orElseThrow(() -> new RuntimeException("Trainer not found with mobile: " + mobile));
    }

    public List<TrainerDTO> appliedTrainers() {
        return trainerRepo.findAll();
    }

    public void deleteTrainer(Long id) {
        if (!trainerRepo.existsById(id)) {
            throw new RuntimeException("Trainer not found with id: " + id);
        }
        trainerRepo.deleteById(id);
    }

    public TrainerDTO approveTrainer(Long id) {
        TrainerDTO trainer = trainerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found with id: " + id));

        // only if TrainerDTO has status field
        trainer.setStatus("APPROVED");

        emailService.sendResultOfTrainerApplication(
                trainer.getEmail(),
                trainer.getName(),
                trainer.getStatus()
        );
        return trainerRepo.save(trainer);
    }

    public TrainerDTO rejectTrainer(Long id) {
        TrainerDTO trainer = trainerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found with id: " + id));

        // only if TrainerDTO has status field
        trainer.setStatus("REJECTED");

       try{
           emailService.sendResultOfTrainerApplication(
                   trainer.getEmail(),
                   trainer.getName(),
                   trainer.getStatus()
           );
       }catch (Exception e){
           throw new RuntimeException(e);
       }

        return trainerRepo.save(trainer);
    }

    public void sendInterviewMail(com.example.codeFiz.model.InterviewMailRequest request){
        try {
            emailService.sendInterviewMail(
                    request.getToEmail(),
                    request.getCandidateName(),
                    request.getSubject(),
                    request.getInterviewDate(),
                    request.getInterviewTime(),
                    request.getInterviewMode(),
                    request.getMeetingLink(),
                    request.getMessage()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void approveStudent(Long mobile) {
        StudentsDTO student = studentsRepo.findById(mobile)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setApprovalStatus("APPROVED");
        studentsRepo.save(student);
    }

    public void rejectStudent(Long mobile) {
        StudentsDTO student = studentsRepo.findById(mobile)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setApprovalStatus("REJECTED");
        studentsRepo.save(student);
    }

}