package com.parent.AdministrationSystem.service;

import com.parent.AdministrationSystem.entity.CourseMaterial;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseMaterialService {
    CourseMaterial uploadCourseMaterial(Long gradeId, String subjectName, String topic, MultipartFile file);

    List<CourseMaterial> getCourseMaterialsByGrade(Long gradeId);
}
