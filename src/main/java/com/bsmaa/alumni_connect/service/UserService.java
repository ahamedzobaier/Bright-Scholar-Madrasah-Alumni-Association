package com.bsmaa.alumni_connect.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsmaa.alumni_connect.model.Notification;
import com.bsmaa.alumni_connect.model.User;
import com.bsmaa.alumni_connect.repository.NotificationRepository;
import com.bsmaa.alumni_connect.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    public String validateLogin(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                if ("APPROVED".equalsIgnoreCase(user.getStatus())) {
                    return "SUCCESS";
                }
                return "PENDING";
            }
        }
        return "FAILURE";
    }

    /**
     * Handles the initial registration/signup logic. Sets status to PENDING for
     * admin approval.
     */
    public void register(User user) {
        // 1. Mandatory status for your approval logic
        user.setStatus("PENDING");

        // 2. Only set a default if the HTML form didn't provide one
        if (user.getAccountType() == null || user.getAccountType().isEmpty()) {
            user.setAccountType("General Member");
        }

        // 3. Save to MySQL
        userRepository.save(user);
    }

    /**
     * Alias method to prevent "symbol not found" errors in Controller
     */
    public void saveUser(User user) {
        this.register(user);
    }

    /**
     * Admin logic to approve a user and send a notification
     */
    public void approveUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus("APPROVED");
            userRepository.save(user);

            // Create notification for the user
            Notification n = new Notification();
            n.setRecipientUsername(user.getUsername());
            n.setMessage("Congratulations! Your account has been approved.");
            n.setLink("/profile");
            notificationRepository.save(n);
        }
    }
}
