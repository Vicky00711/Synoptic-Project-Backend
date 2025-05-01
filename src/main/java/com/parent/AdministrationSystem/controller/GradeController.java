package com.parent.AdministrationSystem.controller;

import com.parent.AdministrationSystem.entity.CourseMaterial;
import com.parent.AdministrationSystem.entity.GradeLevel;
import com.parent.AdministrationSystem.entity.Students;
import com.parent.AdministrationSystem.repository.CourseMaterialRepo;
import com.parent.AdministrationSystem.repository.GradeRepository;
import com.parent.AdministrationSystem.service.GradeLevelService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class GradeController {

    @Autowired
    private GradeLevelService gradeLevelService;

    @Autowired
    private CourseMaterialRepo courseMaterialRepo;

    @Autowired
    private GradeRepository gradeRepository;

    @PostMapping("/grade-level")
    public ResponseEntity<GradeLevel> createGradeLevel(@RequestBody GradeLevel gradeLevel) {
        GradeLevel saved = gradeLevelService.addGradeLevel(gradeLevel);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/grade-levels")
    public ResponseEntity<List<GradeLevel>> getAllGradeLevels() {
        List<GradeLevel> gradeLevels = gradeLevelService.getAllGradeLevels();
        return ResponseEntity.ok(gradeLevels);
    }

    @PostMapping(value = "/upload-timetable/{gradeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadTimetable(
            @PathVariable Long gradeId,
            @RequestParam("file") MultipartFile file) {



        gradeLevelService.uploadTimetable(gradeId, file);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("Timetable uploaded successfully for Grade ID: " + gradeId);
    }

    @DeleteMapping("/grade-level/{gradeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteGradeLevel(@PathVariable Long gradeId) {


        gradeLevelService.deleteGradeLevel(gradeId);
        return ResponseEntity.ok("Grade level deleted successfully");
    }

    @GetMapping("/grade-level/{gradeId}/students")
    public ResponseEntity<List<Students>> getStudentsInGrade(@PathVariable Long gradeId) {
        List<Students> students = gradeLevelService.getStudentsByGradeLevel(gradeId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/grade-level/{gradeId}/timetable")
    public ResponseEntity<Resource> downloadTimetable(@PathVariable Long gradeId) throws IOException {
        GradeLevel gradeLevel = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("Grade level not found"));

        String filePath = gradeLevel.getTimetableFilePath();
        if (filePath == null || filePath.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName().toString() + "\"")
                .body(resource);
    }

}
