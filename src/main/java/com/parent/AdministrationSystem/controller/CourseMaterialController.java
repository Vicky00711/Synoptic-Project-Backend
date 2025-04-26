package com.parent.AdministrationSystem.controller;

import com.parent.AdministrationSystem.entity.CourseMaterial;
import com.parent.AdministrationSystem.service.CourseMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/course-materials")
public class CourseMaterialController {

    @Autowired
    private final CourseMaterialService courseMaterialService;

    public CourseMaterialController(CourseMaterialService courseMaterialService) {
        this.courseMaterialService = courseMaterialService;
    }

    // Upload course material to a grade
    @PostMapping("/upload/{gradeId}")
    public ResponseEntity<CourseMaterial> uploadCourseMaterial(
            @PathVariable Long gradeId,
            @RequestParam("subjectName") String subjectName,
            @RequestParam("topic") String topic,
            @RequestParam("file") MultipartFile file) {

        CourseMaterial material = courseMaterialService.uploadCourseMaterial(gradeId, subjectName, topic, file);
        return ResponseEntity.ok(material);
    }

    // Get all course materials for a grade
    @GetMapping("/grade/{gradeId}")
    public ResponseEntity<List<CourseMaterial>> getMaterialsByGrade(@PathVariable Long gradeId) {
        List<CourseMaterial> materials = courseMaterialService.getCourseMaterialsByGrade(gradeId);
        return ResponseEntity.ok(materials);
    }
}
