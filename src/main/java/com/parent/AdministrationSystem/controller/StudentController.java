package com.parent.AdministrationSystem.controller;

import com.parent.AdministrationSystem.dto.StudentDto;
import com.parent.AdministrationSystem.dto.StudentProfileDTO;
import com.parent.AdministrationSystem.dto.UsersDto;
import com.parent.AdministrationSystem.entity.Students;
import com.parent.AdministrationSystem.service.StudentService;
import com.parent.AdministrationSystem.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
//@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping(consumes = {"application/json", "application/json;charset=UTF-8"})
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<StudentDto> createStudents(@RequestBody StudentDto studentDto) {
        StudentDto savedStudents = studentService.createStudents(studentDto);
        return new ResponseEntity<>(savedStudents, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Students>> getAllStudents() {
        List<Students> students = studentService.listOfStudents();
        return ResponseEntity.ok(students);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteStudents(@PathVariable("id") Long studentId) {
        studentService.deleteStudents(studentId);
        return ResponseEntity.ok("Student has been successfully deleted");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable("id") Long studentId) {
        StudentDto studentProfile = studentService.findProfile(studentId);
        return new ResponseEntity<>(studentProfile, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<StudentDto> profileUpdate(@PathVariable("id") Long studentId, @RequestBody StudentDto studentDto) {
        StudentDto studentProfile = studentService.updateProfile(studentId, studentDto);
        return new ResponseEntity<>(studentProfile, HttpStatus.OK);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentProfileDTO> getOwnProfile() {
        // This endpoint will retrieve the profile of the currently logged-in student
        StudentProfileDTO currentStudentProfile = studentService.findCurrentStudentProfile();
        return new ResponseEntity<>(currentStudentProfile, HttpStatus.OK);
    }
}
