package com.parent.AdministrationSystem.repository;

import com.parent.AdministrationSystem.entity.GradeLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GradeRepository extends JpaRepository<GradeLevel, Long> {
    @Query("SELECT g FROM GradeLevel g JOIN FETCH g.students WHERE g.id = :gradeId")
    Optional<GradeLevel> findGradeLevelStudents(@Param("gradeId") Long gradeId);
}
