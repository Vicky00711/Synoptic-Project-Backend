package com.parent.AdministrationSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseMaterialDTO {
    private Long id;
    private String subjectName;
    private String topic;
    private String materialPath; // The path to download the material (PDF etc.)
}
