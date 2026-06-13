package com.bsmaa.alumni_connect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.bsmaa.alumni_connect.service.FileUploadService;
import java.io.IOException;

import com.bsmaa.alumni_connect.model.Achievement;
import com.bsmaa.alumni_connect.model.BlogPost;
import com.bsmaa.alumni_connect.model.Event;
import com.bsmaa.alumni_connect.model.User;
import com.bsmaa.alumni_connect.repository.AchievementRepository;
import com.bsmaa.alumni_connect.repository.BlogPostRepository;
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

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

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

        // Fetch upcoming events (limit to 3)
        List<Event> upcomingEvents = eventRepository.findAll().stream()
            .filter(e -> "UPCOMING".equalsIgnoreCase(e.getStatus()))
            .sorted((e1, e2) -> e1.getEventDate().compareTo(e2.getEventDate()))
            .limit(3)
            .toList();
        model.addAttribute("events", upcomingEvents);

        // Fetch recent published blogs (limit to 3)
        List<BlogPost> recentBlogs = blogPostRepository.findAll().stream()
            .filter(b -> "PUBLISHED".equalsIgnoreCase(b.getStatus()))
            .sorted((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()))
            .limit(3)
            .toList();
        model.addAttribute("blogs", recentBlogs);

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
        model.addAttribute("achievements", achievementRepository.findAllByOrderByYearDesc());
        return "user/achievement";
    }

    @GetMapping("/blog")
    public String blog(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // Security Check
        }
        model.addAttribute("user", user);
        model.addAttribute("blogs", blogPostRepository.findByStatusOrderByCreatedAtDesc("PUBLISHED"));
        return "user/blog_post";
    }

    @GetMapping("/committee")
    public String committee(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // Security Check
        }
        model.addAttribute("user", user);
        List<User> members = userRepository.findAll().stream()
            .filter(u -> "APPROVED".equalsIgnoreCase(u.getStatus()) && 
                        ("Committee".equalsIgnoreCase(u.getAccountType()) || "Advisor".equalsIgnoreCase(u.getAccountType())))
            .toList();
        model.addAttribute("committeeMembers", members);
        return "user/committee";
    }

    @PostMapping("/blog/submit")
    public String submitBlog(BlogPost post, HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        post.setAuthorUsername(user.getUsername());
        post.setAuthorName(user.getUsername());
        post.setStatus("PENDING");
        blogPostRepository.save(post);
        ra.addFlashAttribute("message", "Story submitted! Awaiting administrator approval.");
        return "redirect:/blog";
    }

    @GetMapping("/blog/view/{id}")
    public String viewBlog(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        BlogPost post = blogPostRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        model.addAttribute("post", post);
        return "user/blog_detail";
    }

    @GetMapping("/event/view/{id}")
    public String viewEvent(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid event Id:" + id));
        model.addAttribute("event", event);
        return "user/event_detail";
    }

    @PostMapping("/profile/upload-pic")
    public String uploadProfilePic(@RequestParam("file") MultipartFile file, HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        try {
            String fileUrl = fileUploadService.uploadFile(file, "profiles");
            if (fileUrl != null) {
                User dbUser = userRepository.findById(user.getUserId()).orElseThrow();
                dbUser.setProfilePicUrl(fileUrl);
                userRepository.save(dbUser);
                session.setAttribute("loggedInUser", dbUser);
                ra.addFlashAttribute("message", "Profile picture updated successfully!");
            }
        } catch (IOException e) {
            ra.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
        }
        return "redirect:/profile";
    }
}
