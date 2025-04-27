package com.parent.AdministrationSystem.controller;

import com.parent.AdministrationSystem.dto.CourseMaterialDTO;
import com.parent.AdministrationSystem.dto.StudentDto;
import com.parent.AdministrationSystem.dto.StudentProfileDTO;
import com.parent.AdministrationSystem.dto.UsersDto;
import com.parent.AdministrationSystem.entity.CourseMaterial;
import com.parent.AdministrationSystem.entity.Students;
import com.parent.AdministrationSystem.repository.StudentRepository;
import com.parent.AdministrationSystem.security.SecurityUtils;
import com.parent.AdministrationSystem.service.CourseMaterialService;
import com.parent.AdministrationSystem.service.StudentService;
import com.parent.AdministrationSystem.service.UsersService;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@RestController
//@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final SecurityUtils securityUtils;
    private final StudentRepository studentsRepository;
    private final CourseMaterialService courseMaterialService;


    public StudentController(StudentService studentService, SecurityUtils securityUtils, StudentRepository studentsRepository, CourseMaterialService courseMaterialService) {
        this.studentService = studentService;
        this.securityUtils = securityUtils;
        this.studentsRepository = studentsRepository;
        this.courseMaterialService = courseMaterialService;
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
    // Controller
    @GetMapping("/profile/course-materials")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CourseMaterialDTO>> getStudentCourseMaterials() {

        String email = securityUtils.getCurrentUserEmail(); // Fetch current student's email
        Students student = studentsRepository.findByUsersEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Long gradeId = student.getGradeLevel().getId();
        List<CourseMaterial> materials = courseMaterialService.getCourseMaterialsByGrade(gradeId);

        List<CourseMaterialDTO> dtos = materials.stream()
                .map(material -> new CourseMaterialDTO(
                        material.getId(),
                        material.getSubjectName(),
                        material.getTopic(),
                        material.getFilePath()

                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("course-materials/download/{materialId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<byte[]> downloadCourseMaterial(@PathVariable Long materialId) {
        try {
            Path filePath = courseMaterialService.getCourseMaterialPath(materialId);

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            byte[] fileBytes = Files.readAllBytes(filePath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename(filePath.getFileName().toString())
                    .build());

            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





}
