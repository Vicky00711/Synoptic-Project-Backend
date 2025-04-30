package com.parent.AdministrationSystem.service.implementation;

import com.parent.AdministrationSystem.entity.CourseMaterial;
import com.parent.AdministrationSystem.entity.GradeLevel;
import com.parent.AdministrationSystem.repository.CourseMaterialRepo;
import com.parent.AdministrationSystem.repository.GradeRepository;
import com.parent.AdministrationSystem.service.CourseMaterialService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
@Service
public class CourseMaterialServiceImplementation implements CourseMaterialService {

    @Autowired
    private CourseMaterialRepo courseMaterialRepository;

    @Autowired
    private GradeRepository gradeRepository;

    private final String uploadDirectory = "uploads/course_materials/";

    @Override
    public CourseMaterial uploadCourseMaterial(Long gradeId, String subjectName, String topic, MultipartFile file) {
        GradeLevel gradeLevel = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new EntityNotFoundException("No grade level found"));

        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDirectory + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            CourseMaterial material = new CourseMaterial();
            material.setGradeLevel(gradeLevel);
            material.setSubjectName(subjectName);
            material.setTopic(topic);
            material.setFilePath(path.toString());

            return courseMaterialRepository.save(material);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload course material", e);
        }
    }



    @Override
    public Resource downloadCourseMaterial(Long materialId) {
        CourseMaterial material = courseMaterialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("No course material found"));

        try {
            Path path = Paths.get(material.getFilePath());
            Resource resource = (Resource)new UrlResource(path.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + material.getFilePath());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading file", e);
        }
    }

    @Override
    public Path getCourseMaterialPath(Long materialId) {
        CourseMaterial material = courseMaterialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Course material not found"));
        return Paths.get(material.getFilePath());
    }


    @Override
    public List<CourseMaterial> getCourseMaterialsByGrade(Long gradeId) {
        return courseMaterialRepository.findByGradeLevel_Id(gradeId);
    }
}
