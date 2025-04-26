package com.parent.AdministrationSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentProfileDTO {

    private Long studentId;
    private String firstName;
    private String lastName;
    private String email;
    private long parentContact;
    private String enrollmentDate;
    private Long gradeLevelId;
    private String gradeLevelName;

}
