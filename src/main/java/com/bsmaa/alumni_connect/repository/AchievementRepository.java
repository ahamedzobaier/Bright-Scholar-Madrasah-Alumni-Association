package com.bsmaa.alumni_connect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bsmaa.alumni_connect.model.Achievement;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    // Fetch all achievements sorted by year (newest first)
    List<Achievement> findAllByOrderByYearDesc();

    // Filter achievements by category (e.g., Entrepreneurship)
    List<Achievement> findByCategory(String category);

    // Search achievements by the name of the winner
    List<Achievement> findByWinnerNameContainingIgnoreCase(String name);
}
