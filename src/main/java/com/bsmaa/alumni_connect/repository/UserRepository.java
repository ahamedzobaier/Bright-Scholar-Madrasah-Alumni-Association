package com.bsmaa.alumni_connect.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bsmaa.alumni_connect.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Existing methods
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    // --- ADD THESE FOR THE ADMIN PANEL ---
    // 1. Find users by status (e.g., PENDING or APPROVED)
    List<User> findByStatus(String status);

    // 2. Count users by status for the Dashboard "Pending" badge
    long countByStatus(String status);

    // 3. Search members by department or batch (Useful for Member Directory)
    List<User> findByDepartment(String department);
}
