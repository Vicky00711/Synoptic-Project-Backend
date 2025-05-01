package com.parent.AdministrationSystem.service.implementation;

import com.parent.AdministrationSystem.dto.UsersDto;
import com.parent.AdministrationSystem.entity.Users;
import com.parent.AdministrationSystem.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class UsersServiceImplementationTest {

    private UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;
    private UsersServiceImplementation usersService;

    private Users user;
    private Users user2;
    private UsersDto userDto;
    private UsersDto userDto2;

    @BeforeEach
    void setUp() {
        usersRepository = mock(UsersRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        usersService = new UsersServiceImplementation(usersRepository, passwordEncoder);

        user = new Users();
        user.setUsersId(1L);
        user.setFirstName("Prateek");
        user.setLastName("sahoo");
        user.setEmail("sahoo@gmail.com");
        user.setPassword("password1");
        user.setRole(Users.Level.ADMIN);

        user2 = new Users();
        user2.setUsersId(2L);
        user2.setFirstName("Vicky");
        user2.setLastName("guerero");
        user2.setEmail("vicky@yahoo.com");
        user2.setPassword("password");
        user2.setRole(Users.Level.STUDENT);

        userDto = new UsersDto(1L, "Prateek", "sahoo", "sahoo@gmail.com", "password1", Users.Level.ADMIN);
        userDto2 = new UsersDto(2L, "Vicky", "guerero", "vicky@yahoo.com", "password", Users.Level.STUDENT);
    }

    @Test
    void shouldCreateUserWithEncodedPassword() {
        when(passwordEncoder.encode("password1")).thenReturn("encodedPass");
        when(usersRepository.save(any(Users.class))).thenAnswer(inv -> inv.getArgument(0));

        UsersDto result = usersService.createUsers(userDto);

        ArgumentCaptor<Users> captor = ArgumentCaptor.forClass(Users.class);
        verify(usersRepository).save(captor.capture());

        assertAll(
                () -> assertThat(captor.getValue().getPassword()).isEqualTo("encodedPass"),
                () -> assertThat(result.getPassword()).isNull()
        );
    }

    @Test
    void shouldReturnUserById() {
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        UsersDto result = usersService.getUserById(1L);

        assertAll(
                () -> assertThat(result.getEmail()).isEqualTo("sahoo@gmail.com"),
                () -> assertThat(result.getFirstName()).isEqualTo("Prateek")
        );
    }

    @Test
    void shouldThrowExceptionIfUserNotFoundById() {
        when(usersRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usersService.getUserById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void shouldDeleteUser() {
        when(usersRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usersRepository).deleteById(1L);

        usersService.deleteUser(1L);

        verify(usersRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeleteUserNotFound() {
        when(usersRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> usersService.deleteUser(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void shouldUpdateUserFields() {
        UsersDto updateDto = new UsersDto();
        updateDto.setFirstName("Updated");
        updateDto.setPassword("newpass");

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedPass");
        when(usersRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UsersDto result = usersService.updateUser(1L, updateDto);

        assertAll(
                () -> assertThat(result.getFirstName()).isEqualTo("Updated"),
                () -> assertThat(result.getPassword()).isNull()
        );
    }

    @Test
    void shouldReturnAllUsers() {
        List<Users> mockUsers = Arrays.asList(user, user2);
        when(usersRepository.findAll()).thenReturn(mockUsers);

        List<UsersDto> result = usersService.getAllUsers();

        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getEmail()).isEqualTo("sahoo@gmail.com"),
                () -> assertThat(result.get(1).getEmail()).isEqualTo("vicky@yahoo.com")
        );
    }
}
