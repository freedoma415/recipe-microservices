package com.example.notification_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // Inject the JavaMailSender
    private final JavaMailSender mailSender;

    public NotificationListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "recipe-event", groupId = "notification-group")
    public void handleRecipeEvent(String message) {
        System.out.println("🔔 NOTIFICATION SERVICE RECEIVED: " + message);
    }
    
    @KafkaListener(topics = {"review-event", "review-events"}, groupId = "notification-group")
public void handleReviewEvent(String message) {
    try {
        
        ReviewEvent event = objectMapper.readValue(message, ReviewEvent.class);
        
        log.info("🔔 NOTIFICATION ALERT: {} left a {} star review on Recipe ID: {}!", 
                event.reviewerName(), event.rating(), event.recipeId());

        // Send the actual email
        sendEmailAlert(event);
        
        Thread.sleep(10000);
        
        
        
    } catch (Exception e) {
        log.error("Failed to parse review event JSON: {}", message, e);
    }
}

    private void sendEmailAlert(ReviewEvent event) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("noreply@recipe-microservices.com");
        mailMessage.setTo("admin@recipe-microservices.com"); // In a real app, query the recipe owner's email
        mailMessage.setSubject("New " + event.rating() + "-Star Review!");
        mailMessage.setText("You received a new review from " + event.reviewerName() + 
                            "\nRecipe ID: " + event.recipeId() + 
                            "\nRating: " + event.rating() + " Stars");
        
        mailSender.send(mailMessage);
        log.info("📧 Email successfully dispatched for Recipe ID: {}", event.recipeId());
    }
}