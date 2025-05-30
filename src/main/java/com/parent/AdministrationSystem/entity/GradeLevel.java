package com.parent.AdministrationSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "grade_level")
public class GradeLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String timetableFilePath;

    @OneToMany(mappedBy = "gradeLevel", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<Students> students = new ArrayList<>();

    @OneToMany(mappedBy = "gradeLevel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CourseMaterial> courseMaterials;


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimetableFilePath() {
        return timetableFilePath;
    }

    public void setTimetableFilePath(String timetableFilePath) {
        this.timetableFilePath = timetableFilePath;
    }

    public List<Students> getStudents() {
        return students;
    }

    public void setStudents(List<Students> students) {
        this.students = students;
    }

    public List<CourseMaterial> getCourseMaterials() {
        return courseMaterials;
    }

    public void setCourseMaterials(List<CourseMaterial> courseMaterials) {
        this.courseMaterials = courseMaterials;
    }
}
