package com.example.codeFiz.service;

import com.example.codeFiz.model.Course;
import com.example.codeFiz.model.StudentsDTO;
import com.example.codeFiz.model.TrainerDTO;
import com.example.codeFiz.repository.CourseRepo;
import com.example.codeFiz.repository.StudentsRepo;
import com.example.codeFiz.repository.TrainerRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepo courseRepo;
    private final StudentsRepo studentsRepo;
    private final TrainerRepo trainerRepo;

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

    public StudentsDTO enrollStudents(String name,
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
        return studentsRepo.save(student);
    }

    public StudentsDTO updateStudent(Long id, StudentsDTO updatedStudent) {
        StudentsDTO existingStudent = studentsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

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

        return trainerRepo.save(trainer);
    }

    public TrainerDTO rejectTrainer(Long id) {
        TrainerDTO trainer = trainerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found with id: " + id));

        // only if TrainerDTO has status field
        trainer.setStatus("REJECTED");

        return trainerRepo.save(trainer);
    }

}