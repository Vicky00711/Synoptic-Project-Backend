package com.parent.AdministrationSystem.service;

import com.parent.AdministrationSystem.dto.GradeDTO;
import com.parent.AdministrationSystem.entity.GradeLevel;
import com.parent.AdministrationSystem.entity.Students;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


    public interface GradeLevelService {
        GradeLevel addGradeLevel(GradeLevel gradeLevel);
        List<GradeLevel> getAllGradeLevels();
        void uploadTimetable(Long gradeId, MultipartFile file);

        List<Students> getStudentsByGradeLevel(Long gradeId);

        void deleteGradeLevel(Long gradeId);


    }


