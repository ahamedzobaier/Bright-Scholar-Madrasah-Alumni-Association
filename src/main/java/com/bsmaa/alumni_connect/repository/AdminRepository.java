package com.bsmaa.alumni_connect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bsmaa.alumni_connect.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByUsername(String username);
}
