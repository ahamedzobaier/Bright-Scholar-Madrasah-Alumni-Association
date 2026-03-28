package com.bsmaa.alumni_connect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bsmaa.alumni_connect.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Find notifications for the logged-in user, newest first
    List<Notification> findByRecipientUsernameOrderByCreatedAtDesc(String username);

    // Count unread notifications for the navbar badge
    long countByRecipientUsernameAndIsReadFalse(String username);
}
