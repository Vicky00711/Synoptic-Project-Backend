package com.parent.AdministrationSystem.service.implementation;

import com.parent.AdministrationSystem.dto.StudentDto;
import com.parent.AdministrationSystem.dto.StudentProfileDTO;
import com.parent.AdministrationSystem.dto.UsersDto;
import com.parent.AdministrationSystem.entity.GradeLevel;
import com.parent.AdministrationSystem.entity.Students;
import com.parent.AdministrationSystem.entity.Users;
import com.parent.AdministrationSystem.repository.GradeRepository;
import com.parent.AdministrationSystem.repository.StudentRepository;
import com.parent.AdministrationSystem.repository.UsersRepository;
import com.parent.AdministrationSystem.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

public class StudentsServiceImplementationTest {

    private UsersRepository usersRepository;
    private StudentRepository studentRepository;
    private GradeRepository gradeRepository;
    private SecurityUtils securityUtils;
    private StudentsServiceImplementation studentsService;

    private Users prateek;
    private GradeLevel grade10;
    private Students student;

    //set up for creating mock data
    @BeforeEach
    void setUp() {
        usersRepository = mock(UsersRepository.class);
        studentRepository = mock(StudentRepository.class);
        gradeRepository = mock(GradeRepository.class);
        securityUtils = mock(SecurityUtils.class);
        studentsService = new StudentsServiceImplementation(usersRepository, studentRepository, gradeRepository, securityUtils);


        prateek = new Users();
        prateek.setUsersId(1L);
        prateek.setFirstName("Prateek");
        prateek.setLastName("Yadav");
        prateek.setEmail("prateek@example.com");
        prateek.setPassword("secure123");

        // Create mock grade
        grade10 = new GradeLevel();
        grade10.setId(1L);
        grade10.setName("Grade 10");

        // Create mock student
        student = new Students();
        student.setStudentId(1L);
        student.setUsers(prateek);
        student.setGradeLevel(grade10);
        student.setParentContact(9876543210L);
        student.setEnrollmentDate(LocalDate.of(2023, 9, 1));
    }

    @Test
    void shouldCreateStudent() {
        StudentDto dto = new StudentDto(null, prateek, 9876543210L, LocalDate.of(2023, 9, 1), grade10);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(prateek));
        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade10));
        when(studentRepository.save(any(Students.class))).thenAnswer(inv -> inv.getArgument(0));

        StudentDto result = studentsService.createStudents(dto);

        assertAll(
                () -> assertThat(result.getUsers()).isEqualTo(prateek),
                () -> assertThat(result.getGradeLevel()).isEqualTo(grade10)
        );
    }

    @Test
    void shouldThrowIfUserNotFoundWhenCreatingStudent() {
        StudentDto dto = new StudentDto(null, prateek, 9876543210L, LocalDate.of(2023, 9, 1), grade10);

        when(usersRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentsService.createStudents(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void shouldDeleteStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentsService.deleteStudents(1L);

        verify(studentRepository).delete(student);
    }

    @Test
    void shouldThrowIfStudentNotFoundWhenDeleting() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentsService.deleteStudents(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Student not found");
    }

    @Test
    void shouldReturnStudentProfileById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentDto result = studentsService.findProfile(1L);

        assertAll(
                () -> assertThat(result.getUsers().getFirstName()).isEqualTo("Prateek")
        );
    }

    @Test
    void shouldUpdateStudentProfile() {
        prateek.setFirstName("UpdatedPrateek");
        StudentDto updateDto = new StudentDto(null, prateek, 1234509876L, LocalDate.of(2024, 1, 10), grade10);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Students.class))).thenAnswer(inv -> inv.getArgument(0));

        StudentDto result = studentsService.updateProfile(1L, updateDto);

        assertAll(
                () -> assertThat(result.getParentContact()).isEqualTo(1234509876L),
                () -> assertThat(result.getUsers().getFirstName()).isEqualTo("UpdatedPrateek")
        );
    }

    @Test
    void shouldReturnListOfStudents() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student));

        List<Students> result = studentsService.listOfStudents();

        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result.get(0).getUsers().getEmail()).isEqualTo("prateek@example.com")
        );
    }

    @Test
    void shouldReturnCurrentStudentProfile() {
        when(securityUtils.getCurrentUserEmail()).thenReturn("prateek@example.com");
        when(studentRepository.findByUsersEmail("prateek@example.com")).thenReturn(Optional.of(student));

        StudentProfileDTO result = studentsService.findCurrentStudentProfile();

        assertAll(
                () -> assertThat(result.getEmail()).isEqualTo("prateek@example.com"),
                () -> assertThat(result.getGradeLevelName()).isEqualTo("Grade 10")
        );
    }
}
