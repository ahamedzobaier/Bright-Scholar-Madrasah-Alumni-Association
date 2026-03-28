package com.bsmaa.alumni_connect.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bsmaa.alumni_connect.model.Admin;
import com.bsmaa.alumni_connect.model.Blog;
import com.bsmaa.alumni_connect.model.Event;
import com.bsmaa.alumni_connect.model.User;
import com.bsmaa.alumni_connect.repository.BlogRepository;
import com.bsmaa.alumni_connect.repository.EventRepository;
import com.bsmaa.alumni_connect.repository.UserRepository;
import com.bsmaa.alumni_connect.service.AdminService;
import com.bsmaa.alumni_connect.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin") // All URLs in this class start with /admin
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BlogRepository blogRepository;

    // --- HELPER METHOD: Security Check ---
    private boolean isAdminNotLoggedIn(HttpSession session) {
        return session.getAttribute("loggedInAdmin") == null;
    }

    // 1. Show Login Page (Public within /admin)
    @GetMapping("/login")
    public String showLoginPage() {
        return "admin/admin_login";
    }

    // 2. Process Login
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        Optional<Admin> admin = adminService.authenticate(username, password);
        if (admin.isPresent()) {
            session.setAttribute("isAdminLoggedIn", true);
            session.setAttribute("loggedInAdmin", admin.get());
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("error", "Invalid Admin Credentials");
        return "admin/admin_login";
    }

    // 3. Dashboard (Secured)
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (isAdminNotLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        List<User> pendingUsers = userRepository.findByStatus("PENDING");
        model.addAttribute("users", pendingUsers);
        model.addAttribute("activePage", "dashboard");
        return "admin/admin_dashboard";
    }

    // 4. Member Management (Secured)
    @GetMapping("/members")
    public String listMembers(HttpSession session, Model model) {
        if (isAdminNotLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        List<User> allMembers = userRepository.findAll();
        model.addAttribute("users", allMembers);
        model.addAttribute("activePage", "members");
        return "admin/member_management";
    }

    // 5. Delete Member (Secured)
    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam Long userId, HttpSession session, RedirectAttributes ra) {
        if (isAdminNotLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        userRepository.deleteById(userId);
        ra.addFlashAttribute("message", "Member removed from directory.");
        return "redirect:/admin/members";
    }

    // 6. Update Member (Secured)
    @PostMapping("/users/update")
    public String updateUser(@RequestParam Long userId,
            @RequestParam String username,
            @RequestParam String department,
            HttpSession session,
            RedirectAttributes ra) {
        if (isAdminNotLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        User user = userRepository.findById(userId).orElseThrow();
        user.setUsername(username);
        user.setDepartment(department);
        userRepository.save(user);
        ra.addFlashAttribute("message", "Profile updated successfully!");
        return "redirect:/admin/members";
    }

    // 7. Approve User (Secured)
    @PostMapping("/users/approve")
    public String approveUser(@RequestParam Long userId, HttpSession session, RedirectAttributes ra) {
        if (isAdminNotLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        userService.approveUser(userId);
        ra.addFlashAttribute("message", "User approved successfully!");
        return "redirect:/admin/dashboard";
    }

    // 8. Reject/Delete User (Secured)
    @PostMapping("/users/reject")
    public String rejectUser(@RequestParam Long userId, HttpSession session, RedirectAttributes ra) {
        if (isAdminNotLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        userRepository.deleteById(userId);
        ra.addFlashAttribute("message", "Registration request rejected.");
        return "redirect:/admin/dashboard";
    }

    // 9. Manage Events List (FIXED PATH & SECURED)
    @GetMapping("/events")
    public String adminEvents(HttpSession session, Model model) {
        if (isAdminNotLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        List<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);
        model.addAttribute("activePage", "events");
        return "admin/events";
    }

    // 10. Show Create Event Form (Secured)
    @GetMapping("/events/new")
    public String showCreateEventForm(HttpSession session, Model model) {
        if (isAdminNotLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        model.addAttribute("event", new Event());
        return "admin/event_form";
    }

    // 11. Save Event (Secured)
    @PostMapping("/events/save")
    public String saveEvent(@ModelAttribute("event") Event event, HttpSession session, RedirectAttributes ra) {
        if (isAdminNotLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        eventRepository.save(event);
        ra.addFlashAttribute("message", "Event published successfully!");
        return "redirect:/admin/events";
    }

    // 12. Blog Management (Secured)
    @GetMapping("/blogs")
    public String listBlogs(HttpSession session, Model model) {
        if (isAdminNotLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        List<Blog> allBlogs = blogRepository.findAll();
        model.addAttribute("blogs", allBlogs);
        model.addAttribute("activePage", "blogs");
        return "admin/blog_management";
    }

    // 13. Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login?logout=true";
    }
}
