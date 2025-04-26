package com.parent.AdministrationSystem.security;

import com.parent.AdministrationSystem.entity.Users;
import com.parent.AdministrationSystem.repository.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminCreation {

    @Bean
    public CommandLineRunner initializeAdmin(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if admin already exists
            if (usersRepository.findByEmail("admin@system.com").isEmpty()) {
                Users adminUser = new Users();
                adminUser.setEmail("admin@system.com");
                adminUser.setPassword(passwordEncoder.encode("Admin@123"));
                adminUser.setFirstName("System");
                adminUser.setLastName("Administrator");
                // Assuming your Users entity has a role field
                adminUser.setRole(Users.Level.ADMIN);
                // Set any other required fields

                usersRepository.save(adminUser);

                System.out.println("Admin user created successfully");
            } else {
                System.out.println("Admin user already exists");
            }
        };
    }
}
