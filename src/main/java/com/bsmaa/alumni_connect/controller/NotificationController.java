package com.bsmaa.alumni_connect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

        // Points to templates/user/notification.html
        return "user/notification";
    }

    @PostMapping("/notifications/mark-all-read")
    public String markAllRead(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        List<Notification> list = notificationRepository.findByRecipientUsernameOrderByCreatedAtDesc(user.getUsername());
        if (!list.isEmpty()) {
            list.forEach(n -> n.setRead(true));
            notificationRepository.saveAll(list);
        }
        return "redirect:/notification";
    }

    @PostMapping("/notifications/read/{id}")
    public String markAsRead(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        notificationRepository.findById(id).ifPresent(n -> {
            if (n.getRecipientUsername().equals(user.getUsername())) {
                n.setRead(true);
                notificationRepository.save(n);
            }
        });
        return "redirect:/notification";
    }
}
