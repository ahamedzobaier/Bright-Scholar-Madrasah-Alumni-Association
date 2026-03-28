package com.bsmaa.alumni_connect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bsmaa.alumni_connect.model.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
}
