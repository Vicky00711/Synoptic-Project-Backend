package com.parent.AdministrationSystem.service.implementation;

import com.parent.AdministrationSystem.dto.StudentDto;
import com.parent.AdministrationSystem.dto.StudentProfileDTO;
import com.parent.AdministrationSystem.entity.GradeLevel;
import com.parent.AdministrationSystem.entity.Students;
import com.parent.AdministrationSystem.entity.Users;
import com.parent.AdministrationSystem.mapper.StudentMapper;
import com.parent.AdministrationSystem.repository.GradeRepository;
import com.parent.AdministrationSystem.repository.StudentRepository;
import com.parent.AdministrationSystem.repository.UsersRepository;
import com.parent.AdministrationSystem.security.SecurityUtils;
import com.parent.AdministrationSystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor  // Automatically injects dependencies via constructor
public class StudentsServiceImplementation implements StudentService {

    private final UsersRepository usersRepository;
    private final StudentRepository studentsRepository;
    private final GradeRepository gradeRepository;
    private final SecurityUtils securityUtils;

    @Override
    public StudentDto createStudents(StudentDto studentDto) {

        Users user = usersRepository.findById(studentDto.getUsers().getUsersId())
                .orElseThrow(() -> new RuntimeException("User not found"));


        GradeLevel gradeLevel = gradeRepository.findById(studentDto.getGradeLevel().getId())
                .orElseThrow(() -> new RuntimeException("Grade level not found"));


        Students student = new Students();
        student.setUsers(user);
        student.setGradeLevel(gradeLevel);
        student.setParentContact(studentDto.getParentContact());
        student.setEnrollmentDate(studentDto.getEnrollmentDate());


        Students savedStudent = studentsRepository.save(student);

        return new StudentDto(
                savedStudent.getStudentId(),
                savedStudent.getUsers(),
                savedStudent.getParentContact(),
                savedStudent.getEnrollmentDate(),
                savedStudent.getGradeLevel()
        );
    }

    @Override
    public void deleteStudents(Long studentId) {

        Students student = studentsRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Hibernate.initialize(student.getUsers());


        studentsRepository.delete(student);
    }

    @Override
    public StudentDto findProfile(Long studentId) {
        Students student = studentsRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Hibernate.initialize(student.getUsers());

        return StudentMapper.mapToStudentDto(student);


    }
    @Override
    public StudentDto updateProfile(Long studentId, StudentDto studentDto) {
        // Find the student
        Students student = studentsRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Users users = student.getUsers();
        if (users == null) {
            throw new RuntimeException("User not found for this student");
        }

        // validation for extra check
        if (studentDto.getUsers() != null) {
            Users userDetails = studentDto.getUsers();

            if (userDetails.getFirstName() != null) {
                users.setFirstName(userDetails.getFirstName());
            }
            if (userDetails.getLastName() != null) {
                users.setLastName(userDetails.getLastName());
            }
            if (userDetails.getEmail() != null) {
                users.setEmail(userDetails.getEmail());
            }
            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                users.setPassword(userDetails.getPassword());
            }
        }

        // update the student fields
        if (studentDto.getGradeLevel() != null) {
            student.setGradeLevel(studentDto.getGradeLevel());
        }
        if (studentDto.getParentContact() > 0) {
            student.setParentContact(studentDto.getParentContact());
        }
        if (studentDto.getEnrollmentDate() != null) {
            student.setEnrollmentDate(studentDto.getEnrollmentDate());
        }

        // Save user first
        usersRepository.save(users);

        // Save student
        Students updatedStudent = studentsRepository.save(student);

        return StudentMapper.mapToStudentDto(updatedStudent);
    }
    @Override
    public List<Students> listOfStudents() {
        List<Students> students = studentsRepository.findAll();
        for (Students student : students) {
            Hibernate.initialize(student.getGradeLevel());
        }
        return students;
    }





    @Override
    public StudentProfileDTO findCurrentStudentProfile() {
        // Get current logged-in user's email
        String email = securityUtils.getCurrentUserEmail();

        // Find student by email
        Students student = studentsRepository.findByUsersEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return new StudentProfileDTO(
                student.getStudentId(),
                student.getUsers().getFirstName(),
                student.getUsers().getLastName(),
                student.getUsers().getEmail(),
                student.getParentContact(),
                student.getEnrollmentDate().toString(),
                student.getGradeLevel().getId(),
                student.getGradeLevel().getName()

        );
    }



}
