package com.bsmaa.alumni_connect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bsmaa.alumni_connect.model.BlogPost;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    // For the public Blog page: Only show approved posts, newest first
    List<BlogPost> findByStatusOrderByCreatedAtDesc(String status);

    // For the user's Profile page: See their own posts (even pending ones)
    List<BlogPost> findByAuthorUsernameOrderByCreatedAtDesc(String username);

    // For the Admin Panel: Find everything waiting for review
    List<BlogPost> findByStatus(String status);
}
