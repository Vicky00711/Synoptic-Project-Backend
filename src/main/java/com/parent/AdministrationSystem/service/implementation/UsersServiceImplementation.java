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
        // Encrypt the password before saving
        usersDto.setPassword(passwordEncoder.encode(usersDto.getPassword()));

        // Convert DTO to entity
        Users users = UsersMapper.mapToUsers(usersDto);
        // Save to repository
        Users savedUsers = usersRepository.save(users);
        // Convert entity back to DTO and return
        return UsersMapper.mapToUsersDto(savedUsers);
    }

    @Override
    public UsersDto getUserById(Long userId) {
        // Find user by ID
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Convert the entity to DTO
        return UsersMapper.mapToUsersDto(users);
    }

    @Override
    public void deleteUser(Long userId) {
        // Check if the user exists before attempting to delete
        if (!usersRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        // Delete the user
        usersRepository.deleteById(userId);
    }

    @Override
    public UsersDto updateUser(Long userId, UsersDto usersDto) {
        // Find the existing user
        Users existingUser = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Update fields only if they are provided
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

        // Save the updated user
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