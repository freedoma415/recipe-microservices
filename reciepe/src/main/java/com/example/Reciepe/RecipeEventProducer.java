package com.example.Reciepe;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RecipeEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public RecipeEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRecipeCreatedEvent(String recipeName) {
        // Sends a message to the "recipe-events" topic
        kafkaTemplate.send("recipe-events", "New Recipe Created: " + recipeName);
        System.out.println("Published event to Kafka: " + recipeName);
    }
}