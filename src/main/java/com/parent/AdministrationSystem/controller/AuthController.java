package com.parent.AdministrationSystem.controller;

import com.parent.AdministrationSystem.dto.AuthRequestDTO;
import com.parent.AdministrationSystem.dto.AuthResponseDTO;
import com.parent.AdministrationSystem.dto.UsersDto;
import com.parent.AdministrationSystem.entity.Users;
import com.parent.AdministrationSystem.security.CustomUserDetails;
import com.parent.AdministrationSystem.security.JWTBlacklistService;
import com.parent.AdministrationSystem.security.JwtUtil;
import com.parent.AdministrationSystem.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:5173")


public class AuthController {

    @Autowired
    private JWTBlacklistService jwtBlacklistService;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsersService usersService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsersService usersService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usersService = usersService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            final String jwt = jwtUtil.generateToken(userDetails);

            Users user = userDetails.getUser();

            AuthResponseDTO response = new AuthResponseDTO(
                    jwt,
                    user.getEmail(),
                    user.getRole().name(),
                    user.getFirstName(),
                    user.getLastName()
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Incorrect email or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsersDto usersDto) {
        try {
            UsersDto savedUser = usersService.createUsers(usersDto);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        // Extract the JWT token from the Authorization header
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove the "Bearer " prefix
            // Blacklist the token
            jwtBlacklistService.blacklistToken(token);
            return ResponseEntity.ok("Logged out successfully");
        } else {
            return ResponseEntity.status(400).body("Token is missing or invalid");
        }
    }


}