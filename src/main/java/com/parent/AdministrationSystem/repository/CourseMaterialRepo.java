package com.parent.AdministrationSystem.repository;

import com.parent.AdministrationSystem.entity.CourseMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseMaterialRepo extends JpaRepository<CourseMaterial, Long> {
    List<CourseMaterial> findByGradeLevel_Id(Long gradeId);
}
