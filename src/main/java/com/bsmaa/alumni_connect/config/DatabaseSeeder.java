package com.bsmaa.alumni_connect.config;

import com.bsmaa.alumni_connect.model.Admin;
import com.bsmaa.alumni_connect.repository.AdminRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DatabaseSeeder {

    @Bean
    public CommandLineRunner seedDatabase(AdminRepository adminRepository) {
        return args -> {
            if (adminRepository.count() == 0) {
                Admin defaultAdmin = new Admin();
                defaultAdmin.setUsername("BSMADMIN");
                // Hash "@BSMadmin" with BCrypt
                defaultAdmin.setPassword(BCrypt.hashpw("@BSMadmin", BCrypt.gensalt()));
                defaultAdmin.setFullName("BSM Administrator");
                defaultAdmin.setEmail("admin@bsm.edu.bd");
                defaultAdmin.setRole("ADMIN");
                defaultAdmin.setCreatedAt(LocalDateTime.now());
                adminRepository.save(defaultAdmin);
                System.out.println("Default administrator account created successfully: BSMADMIN / @BSMadmin");
            }
        };
    }
}
