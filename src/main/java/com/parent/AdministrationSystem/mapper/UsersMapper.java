package com.parent.AdministrationSystem.mapper;

import com.parent.AdministrationSystem.dto.UsersDto;
import com.parent.AdministrationSystem.entity.Users;

public class UsersMapper {

    public static UsersDto mapToUsersDto(Users users) {
        if (users == null) return null;

        UsersDto dto = new UsersDto();
        dto.setUsersId(users.getUsersId());
        dto.setFirstName(users.getFirstName());
        dto.setLastName(users.getLastName());
        dto.setEmail(users.getEmail());
        dto.setPassword(null); // 🚫 Don't expose password
        dto.setRole(users.getRole());

        return dto;
    }

    public static Users mapToUsers(UsersDto usersDto) {
        if (usersDto == null) return null;

        Users users = new Users();
        users.setUsersId(usersDto.getUsersId());
        users.setFirstName(usersDto.getFirstName());
        users.setLastName(usersDto.getLastName());
        users.setEmail(usersDto.getEmail());
        users.setPassword(usersDto.getPassword());
        users.setRole(usersDto.getRole());

        return users;
    }
}
