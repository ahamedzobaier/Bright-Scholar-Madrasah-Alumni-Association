package com.bsmaa.alumni_connect.repository;

import com.bsmaa.alumni_connect.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Used for Admin Panel authentication
    Optional<Admin> findByUsername(String username);
}
