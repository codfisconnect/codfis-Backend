package com.example.codeFiz.controller;

import com.example.codeFiz.model.Course;
import com.example.codeFiz.model.StudentsDTO;
import com.example.codeFiz.model.TrainerDTO;
import com.example.codeFiz.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // =========================
    // COURSE APIs
    // =========================

    @GetMapping("")
    public List<Course> availableCourses() {
        return courseService.availableCourses();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCourse(@RequestBody Course course) {
        try {
            courseService.addCourse(course);
            return ResponseEntity.ok("Course added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add course: " + e.getMessage());
        }
    }

    @PutMapping("/update/{courseId}")
    public ResponseEntity<String> updateCourse(@PathVariable String courseId, @RequestBody Course course) {
        courseService.updateCourse(courseId, course);
        return ResponseEntity.ok("Course updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok("Course deleted successfully");
    }

    // =========================
    // STUDENT APIs
    // =========================

    @GetMapping("/student/all")
    public List<StudentsDTO> enrolledStudents() {
        return courseService.enrolledStudents();
    }

    @GetMapping("/student/enrolled/{courseName}")
    public List<StudentsDTO> searchStudentbycourse(@PathVariable String courseName) {
        return courseService.searchStudentbycourse(courseName);
    }

    @PostMapping("/student/enroll")
    public ResponseEntity<String> enrollStudents(@RequestBody StudentsDTO student) {
        courseService.enrollStudents(
                student.getName(),
                student.getGender(),
                student.getEmail(),
                student.getMobile(),
                student.getCourseName()
        );

        return ResponseEntity.ok(
                "Congratulations! " + student.getName() +
                        " you have successfully registered for " + student.getCourseName() +
                        " we'll call you soon..."
        );
    }

    @PutMapping("/student/update/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable Long id, @RequestBody StudentsDTO student) {
        courseService.updateStudent(id, student);
        return ResponseEntity.ok("Student updated successfully");
    }

    @DeleteMapping("/student/delete/{mobile}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long mobile) {
        courseService.deleteStudent(mobile);
        return ResponseEntity.ok("Student deleted successfully");
    }

    // =========================
    // TRAINER APIs
    // =========================

    @PostMapping(value = "/trainer/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> trainerRegister(
            @RequestParam("name") String name,
            @RequestParam("gender") String gender,
            @RequestParam("email") String email,
            @RequestParam("mobile") String mobile,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file) {

        try {
            courseService.trainerRegister(name, gender, email, mobile, description, file);
            return ResponseEntity.ok("Congratulations " + name + "! Our team will contact you soon.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/trainer/applied")
    public List<TrainerDTO> appliedTrainers() {
        return courseService.appliedTrainers();
    }

    @DeleteMapping("/trainer/delete/{id}")
    public ResponseEntity<String> deleteTrainer(@PathVariable Long id) {
        courseService.deleteTrainer(id);
        return ResponseEntity.ok("Trainer deleted successfully");
    }

    @PutMapping("/trainer/approve/{id}")
    public ResponseEntity<String> approveTrainer(@PathVariable Long id) {
        courseService.approveTrainer(id);
        return ResponseEntity.ok("Trainer approved successfully");
    }

    @PutMapping("/trainer/reject/{id}")
    public ResponseEntity<String> rejectTrainer(@PathVariable Long id) {
        courseService.rejectTrainer(id);
        return ResponseEntity.ok("Trainer rejected successfully");
    }
}