package com.parent.AdministrationSystem.service;

import com.parent.AdministrationSystem.dto.StudentDto;
import com.parent.AdministrationSystem.dto.UsersDto;
import com.parent.AdministrationSystem.entity.Students;

import java.util.List;

public interface StudentService {
     StudentDto createStudents(StudentDto studentDto);
     void deleteStudents(Long studentId);
     StudentDto findProfile(Long studentId);
     StudentDto updateProfile(Long studentId, StudentDto studentDto);
     List<Students> listOfStudents();
     StudentDto findCurrentStudentProfile();
}
