package com.example.notification_service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    @KafkaListener(topics = "recipe-events", groupId = "notification-group")
    public void handleRecipeEvent(String message) {
        // In a real app, you would trigger an email or SMS here
        System.out.println("🔔 NOTIFICATION SERVICE RECEIVED: " + message);
        System.out.println("Sending welcome email to users...");
    }
}