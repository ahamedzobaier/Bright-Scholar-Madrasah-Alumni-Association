package com.bsmaa.alumni_connect.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bsmaa.alumni_connect.entity.User;
import com.bsmaa.alumni_connect.repository.EventRepository;
import com.bsmaa.alumni_connect.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    // Fixed: Handles both root and /home to match your login redirect
    @GetMapping({"/", "/home"})
    public String homePage(Model model) {
        try {
            // Try to fetch events
            model.addAttribute("events", eventRepository.findAll());
        } catch (Exception e) {
            // If DB fails, send an empty list so the HTML doesn't crash
            model.addAttribute("events", new java.util.ArrayList<>());
            System.err.println("Database Error: " + e.getMessage());
        }

        model.addAttribute("pageTitle", "Home - BSMAA");
        return "home";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", loggedInUser); // Ensure this matches ${user.name} in HTML
        return "profile";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("pageTitle", "Login - BSMAA");
        return "login";
    }

    @PostMapping("/login")
    public String loginProcess(@RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            // 1. Save user to session
            session.setAttribute("loggedInUser", userOpt.get());

            // 2. REDIRECT to the home mapping
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Invalid Username or Password");
            return "login"; // Stay on login page if it fails
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        model.addAttribute("pageTitle", "Alumni Blog");
        return "blog";
    }

    @GetMapping("/notifications")
    public String notifications(Model model) {
        model.addAttribute("pageTitle", "Notifications");
        return "notification";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "About Us");
        return "about";
    }

    @GetMapping("/members")
    public String members(Model model) {
        model.addAttribute("pageTitle", "Members & Committee");
        return "members";
    }

    @GetMapping("/events")
    public String allEvents(Model model) {
        model.addAttribute("pageTitle", "Upcoming Events");
        return "events";
    }

    @GetMapping("/achievements")
    public String achievements(Model model) {
        model.addAttribute("pageTitle", "Our Achievements");
        return "achievements";
    }

    @GetMapping("/support")
    public String support(Model model) {
        model.addAttribute("pageTitle", "Support & Feedback");
        return "support-management";
    }
}
