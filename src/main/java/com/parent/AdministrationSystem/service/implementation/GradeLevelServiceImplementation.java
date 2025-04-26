package com.parent.AdministrationSystem.service.implementation;

import com.parent.AdministrationSystem.entity.GradeLevel;
import com.parent.AdministrationSystem.entity.Students;
import com.parent.AdministrationSystem.repository.GradeRepository;
import com.parent.AdministrationSystem.repository.StudentRepository;
import com.parent.AdministrationSystem.service.GradeLevelService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class GradeLevelServiceImplementation implements GradeLevelService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    private final String uploadDirectory = "uploads/timetables/";

    @Override
    public GradeLevel addGradeLevel(GradeLevel gradeLevel) {
        // No need to set timetable path here — it's uploaded later
        return gradeRepository.save(gradeLevel);
    }

    @Override
    public List<GradeLevel> getAllGradeLevels() {
        return gradeRepository.findAll();
    }


    @Override
    public void uploadTimetable(Long gradeId, MultipartFile file) {
        GradeLevel gradeLevel = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new EntityNotFoundException("Grade level not found with ID: " + gradeId));

        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDirectory + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            gradeLevel.setTimetableFilePath(path.toString()); // still updates the entity
            gradeRepository.save(gradeLevel);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload timetable file", e);
        }
    }

    @Override
    public List<Students> getStudentsByGradeLevel(Long gradeId) {
        GradeLevel gradeLevel = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("Grade level not found with ID: " + gradeId));
        return gradeLevel.getStudents();
    }

    @Override
    public void deleteGradeLevel(Long gradeId) {
        GradeLevel gradeLevel = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new EntityNotFoundException("Grade level not found with ID: " + gradeId));

        gradeRepository.delete(gradeLevel);
    }
}
