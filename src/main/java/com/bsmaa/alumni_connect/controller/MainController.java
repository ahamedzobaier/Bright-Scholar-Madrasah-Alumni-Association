package com.bsmaa.alumni_connect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bsmaa.alumni_connect.model.Event;
import com.bsmaa.alumni_connect.model.User;
import com.bsmaa.alumni_connect.repository.EventRepository;
import com.bsmaa.alumni_connect.repository.UserRepository;
import com.bsmaa.alumni_connect.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    // --- PUBLIC ROUTES (No Login Required) ---
    @GetMapping("/login")
    public String showUserLoginPage() {
        return "user/login";
    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "user/signup";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        String status = userService.validateLogin(username, password);
        if ("SUCCESS".equals(status)) {
            userRepository.findByUsername(username).ifPresent(user -> {
                session.setAttribute("loggedInUser", user);
            });
            return "redirect:/home";
        } else if ("PENDING".equals(status)) {
            model.addAttribute("error", "Your account is awaiting Admin approval.");
            return "user/login";
        } else {
            model.addAttribute("error", "Invalid username or password.");
            return "user/login";
        }
    }

    @PostMapping("/signup")
    public String handleSignup(User user, Model model) {
        try {
            userService.register(user);
            model.addAttribute("success", "Application submitted! Wait for admin approval.");
            return "user/login";
        } catch (Exception e) {
            model.addAttribute("error", "Error: Username or Email already exists.");
            return "user/signup";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }

    // --- PRIVATE ROUTES (Login Required) ---
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // Security Check
        }
        model.addAttribute("user", user);
        return "user/home";
    }

    @GetMapping("/about")
    public String about(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // Security Check
        }
        model.addAttribute("user", user);
        return "user/about";
    }

    @GetMapping("/event")
    public String event(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // Security Check
        }
        model.addAttribute("user", user);
        List<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);
        return "user/event";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // Security Check
        }
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/achievement")
    public String achievement(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // Security Check
        }
        model.addAttribute("user", user);
        return "user/achievement";
    }

    @GetMapping("/blog")
    public String blog(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // Security Check
        }
        model.addAttribute("user", user);
        return "user/blog_post";
    }

    @GetMapping("/committee")
    public String committee(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // Security Check
        }
        model.addAttribute("user", user);
        return "user/committee";
    }
}
