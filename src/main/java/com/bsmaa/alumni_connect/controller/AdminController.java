package com.bsmaa.alumni_connect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bsmaa.alumni_connect.entity.Admin;
import com.bsmaa.alumni_connect.entity.Event;
import com.bsmaa.alumni_connect.repository.AdminRepository;
import com.bsmaa.alumni_connect.repository.EventRepository;
import com.bsmaa.alumni_connect.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    // Security check: Checks if the Admin object exists in session
    private boolean isNotAdmin(HttpSession session) {
        return session.getAttribute("loggedInAdmin") == null;
    }

    // 1. Show the login page (File: adminlogin.html)
    @GetMapping("/login")
    public String adminLoginPage() {
        System.out.println("DEBUG: Attempting to load adminlogin.html");
        return "admin/adminlogin";
    }

    // 2. Process Login
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null && admin.getPassword().equals(password)) {
            session.setAttribute("loggedInAdmin", admin);
            return "redirect:/admin/dashboard"; // Successful login
        }
        model.addAttribute("error", "Invalid Admin Credentials");
        return "admin/adminlogin";
    }

    // 3. Dashboard (File: admindashboard.html)
    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/admin/login";
        }

        try {
            long mCount = userRepository.count();
            model.addAttribute("totalMembers", mCount);
            System.out.println("DEBUG: Member Count Successful: " + mCount);
        } catch (Exception e) {
            System.out.println("DEBUG ERROR: UserRepository failed! " + e.getMessage());
            model.addAttribute("totalMembers", "Error");
        }

        try {
            long eCount = eventRepository.count();
            model.addAttribute("totalEvents", eCount);
            System.out.println("DEBUG: Event Count Successful: " + eCount);
        } catch (Exception e) {
            System.out.println("DEBUG ERROR: EventRepository failed! " + e.getMessage());
            model.addAttribute("totalEvents", "Error");
        }

        return "admin/admindashboard";
    }

    // 4. Manage Events (File: manage_event.html)
    @GetMapping("/events/new")
    public String addEventPage(HttpSession session, Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/admin/login";
        }

        model.addAttribute("event", new Event());
        return "admin/manage_event";
    }

    @PostMapping("/events/save")
    public String saveEvent(@ModelAttribute("event") Event event, HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/admin/login";
        }

        eventRepository.save(event);
        return "redirect:/admin/dashboard?success=true";
    }

    // 5. Manage Members (File: manage_member.html)
    @GetMapping("/members")
    public String manageMembers(HttpSession session, Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/admin/login";
        }

        try {
            var membersList = userRepository.findAll();
            model.addAttribute("members", membersList);
            System.out.println("DEBUG: Successfully fetched " + membersList.size() + " members.");
        } catch (Exception e) {
            System.err.println("DATABASE ERROR: " + e.getMessage());
            return "error"; // Or a specific error page
        }

        return "admin/manage_member";
    }

    @GetMapping("/members/delete/{id}")
    public String deleteMember(@PathVariable("id") Long id, HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/admin/login";
        }
        userRepository.deleteById(id);
        return "redirect:/admin/members?deleted=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login?logout=true";
    }
}
