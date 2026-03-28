package com.bsmaa.alumni_connect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsmaa.alumni_connect.model.Achievement;
import com.bsmaa.alumni_connect.model.BlogPost;
import com.bsmaa.alumni_connect.model.Event;
import com.bsmaa.alumni_connect.repository.AchievementRepository;
import com.bsmaa.alumni_connect.repository.BlogPostRepository;
import com.bsmaa.alumni_connect.repository.EventRepository;

@Service
public class ContentService {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private AchievementRepository achRepo;

    @Autowired
    private BlogPostRepository blogRepo;

    // --- EVENT LOGIC ---
    public List<Event> getAllEvents() {
        return eventRepo.findAllByOrderByEventDateAsc();
    }

    public List<Event> getUpcomingEvents() {
        return eventRepo.findByStatusOrderByEventDateAsc("UPCOMING");
    }

    public void saveEvent(Event event) {
        eventRepo.save(event);
    }

    // --- ACHIEVEMENT LOGIC ---
    public List<Achievement> getAllAchievements() {
        return achRepo.findAllByOrderByYearDesc();
    }

    public void saveAchievement(Achievement achievement) {
        achRepo.save(achievement);
    }

    // --- BLOG LOGIC ---
    public List<BlogPost> getAllPublishedBlogs() {
        // Only show blogs approved by admin
        return blogRepo.findByStatusOrderByCreatedAtDesc("PUBLISHED");
    }

    public void saveBlogPost(BlogPost post) {
        // Users submit blogs as "PENDING"
        if (post.getStatus() == null) {
            post.setStatus("PENDING");
        }
        blogRepo.save(post);
    }

    // --- DELETE LOGIC (For Admin) ---
    public void deleteEvent(Long id) {
        eventRepo.deleteById(id);
    }

    public void deleteAchievement(Long id) {
        achRepo.deleteById(id);
    }
}
