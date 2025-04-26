package com.parent.AdministrationSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parent.AdministrationSystem.entity.GradeLevel;
import com.parent.AdministrationSystem.entity.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Students {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private long studentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    @JsonIgnoreProperties({"student", "hibernateLazyInitializer", "handler"})
    private Users users;

    @Column(name = "parent_contact")
    private long parentContact;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_level_id")
    @JsonIgnoreProperties({"students", "hibernateLazyInitializer", "handler"})
    private GradeLevel gradeLevel;
}
