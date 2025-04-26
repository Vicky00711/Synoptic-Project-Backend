package com.parent.AdministrationSystem.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.parent.AdministrationSystem.entity.GradeLevel;
import com.parent.AdministrationSystem.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long studentId;

    @JsonIgnoreProperties({"students"})
    private Users users;
    private long parentContact;
    private LocalDate enrollmentDate;

    @JsonIgnoreProperties({"students"})
    private GradeLevel gradeLevel;


}
