package com.bsmaa.alumni_connect.service;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsmaa.alumni_connect.model.Admin;
import com.bsmaa.alumni_connect.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    /**
     * Verifies admin credentials. Returns the Admin object if successful,
     * otherwise empty.
     */
    public Optional<Admin> authenticate(String username, String password) {
        Optional<Admin> adminOpt = adminRepository.findByUsername(username);

        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            String storedPassword = admin.getPassword();
            boolean passwordMatch = false;
            boolean upgradeNeeded = false;

            if (storedPassword != null && (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$"))) {
                passwordMatch = BCrypt.checkpw(password, storedPassword);
            } else {
                if (password.equals(storedPassword)) {
                    passwordMatch = true;
                    upgradeNeeded = true;
                }
            }

            if (passwordMatch) {
                if (upgradeNeeded) {
                    admin.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
                    adminRepository.save(admin);
                }
                return Optional.of(admin);
            }
        }
        return Optional.empty();
    }
    
}
