package com.parent.AdministrationSystem.service.implementation;

import com.parent.AdministrationSystem.dto.UsersDto;
import com.parent.AdministrationSystem.entity.Users;
import com.parent.AdministrationSystem.mapper.UsersMapper;
import com.parent.AdministrationSystem.repository.UsersRepository;
import com.parent.AdministrationSystem.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersServiceImplementation implements UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersServiceImplementation(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsersDto createUsers(UsersDto usersDto) {
        // password encrypted before saving
        usersDto.setPassword(passwordEncoder.encode(usersDto.getPassword()));

        Users users = UsersMapper.mapToUsers(usersDto);
        Users savedUsers = usersRepository.save(users);
        return UsersMapper.mapToUsersDto(savedUsers);
    }

    @Override
    public UsersDto getUserById(Long userId) {
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UsersMapper.mapToUsersDto(users);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!usersRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        usersRepository.deleteById(userId);
    }

    @Override
    public UsersDto updateUser(Long userId, UsersDto usersDto) {
        Users existingUser = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // update the fields if the fields are provided otherwise no
        if (usersDto.getFirstName() != null) {
            existingUser.setFirstName(usersDto.getFirstName());
        }
        if (usersDto.getLastName() != null) {
            existingUser.setLastName(usersDto.getLastName());
        }
        if (usersDto.getEmail() != null) {
            existingUser.setEmail(usersDto.getEmail());
        }
        if (usersDto.getPassword() != null && !usersDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(usersDto.getPassword()));
        }

        // Saving the user
        Users updatedUser = usersRepository.save(existingUser);

        return UsersMapper.mapToUsersDto(updatedUser);
    }


    @Override
    public List<UsersDto> getAllUsers() {
        List<Users> usersList = usersRepository.findAll();
        return usersList.stream()
                .map(UsersMapper::mapToUsersDto)
                .collect(Collectors.toList());
    }
}