package com.parent.AdministrationSystem.mapper;

import com.parent.AdministrationSystem.dto.StudentDto;
import com.parent.AdministrationSystem.dto.UsersDto;
import com.parent.AdministrationSystem.entity.Students;
import com.parent.AdministrationSystem.entity.Users;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class StudentMapper {

    public static StudentDto mapToStudentDto(Students students) {
        if (students == null) return null;

        return new StudentDto(
                        students.getStudentId(),
                        students.getUsers(),
                students.getParentContact(),
                students.getEnrollmentDate(),
                students.getGradeLevel()

                );
    }

    public static Students mapToStudents(StudentDto studentsDto) {
        if (studentsDto == null) return null;

        return new Students(
                        studentsDto.getStudentId(),
                        studentsDto.getUsers(),
                studentsDto.getParentContact(),
                studentsDto.getEnrollmentDate(),
                studentsDto.getGradeLevel()
                );
    }
}
