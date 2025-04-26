package com.parent.AdministrationSystem.service;

import com.parent.AdministrationSystem.dto.UsersDto;

import java.util.List;

public interface UsersService {
    UsersDto createUsers(UsersDto usersDto);

    UsersDto getUserById(Long userId);

    void deleteUser(Long userId);

    UsersDto updateUser(Long userId, UsersDto usersDto);

    List<UsersDto> getAllUsers();
}