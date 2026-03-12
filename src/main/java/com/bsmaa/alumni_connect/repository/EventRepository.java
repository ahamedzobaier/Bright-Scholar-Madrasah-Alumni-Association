package com.bsmaa.alumni_connect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bsmaa.alumni_connect.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // This gives you save(), findAll(), delete(), etc. for free!
}
