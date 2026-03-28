package com.bsmaa.alumni_connect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bsmaa.alumni_connect.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Fetch all events sorted by date (soonest first)
    List<Event> findAllByOrderByEventDateAsc();

    // Fetch only events with a specific status (e.g., "UPCOMING")
    List<Event> findByStatusOrderByEventDateAsc(String status);

    // Fetch the 3 most recent events for the Home page
    List<Event> findTop3ByOrderByEventDateDesc();
}
