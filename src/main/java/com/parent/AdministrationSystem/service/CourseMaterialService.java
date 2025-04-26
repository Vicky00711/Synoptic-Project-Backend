package com.parent.AdministrationSystem.service;

import com.parent.AdministrationSystem.entity.CourseMaterial;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface CourseMaterialService {
    CourseMaterial uploadCourseMaterial(Long gradeId, String subjectName, String topic, MultipartFile file);

    List<CourseMaterial> getCourseMaterialsByGrade(Long gradeId);

    Resource downloadCourseMaterial(Long materialId);
    Path getCourseMaterialPath(Long materialId);

}
