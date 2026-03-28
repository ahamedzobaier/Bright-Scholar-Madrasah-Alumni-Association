package com.bsmaa.alumni_connect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bsmaa.alumni_connect.model.Notification;
import com.bsmaa.alumni_connect.model.User;
import com.bsmaa.alumni_connect.repository.NotificationRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/notification")
    public String showNotifications(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        // Add user to model so the navbar/sidebar fragments can display profile info
        model.addAttribute("user", user);

        List<Notification> list = notificationRepository.findByRecipientUsernameOrderByCreatedAtDesc(user.getUsername());
        model.addAttribute("notifications", list);

        // Mark all as read when they visit the page
        if (!list.isEmpty()) {
            list.forEach(n -> n.setRead(true));
            notificationRepository.saveAll(list);
        }

        // Points to templates/user/notification.html
        return "user/notification";
    }
}
