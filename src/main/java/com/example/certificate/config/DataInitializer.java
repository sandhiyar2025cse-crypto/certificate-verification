package com.example.certificate.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.certificate.model.User;
import com.example.certificate.model.UserRole;
import com.example.certificate.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.findByUsername("admin").isEmpty()) {

                User admin = new User(
                        "admin",
                        passwordEncoder.encode("admin123"),
                        UserRole.ADMIN
                );

                userRepository.save(admin);

                System.out.println("Default admin user created.");
            }
        };
    }
}