package com.parent.AdministrationSystem.controller;

import com.parent.AdministrationSystem.dto.UsersDto;
import com.parent.AdministrationSystem.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/list-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsersDto>> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsersDto> createUsers(@RequestBody UsersDto usersDto) {
        UsersDto savedUsers = usersService.createUsers(usersDto);
        return new ResponseEntity<>(savedUsers, HttpStatus.CREATED);
    }

    @GetMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsersDto> getUserById(@PathVariable("id") Long userId) {
        UsersDto user = usersService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        usersService.deleteUser(userId);
        return ResponseEntity.ok("User has been successfully deleted");
    }

    @PutMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsersDto> updateUser(@PathVariable("id") Long userId, @RequestBody UsersDto usersDto) {
        UsersDto updatedUser = usersService.updateUser(userId, usersDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}