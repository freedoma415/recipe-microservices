package com.review_service.review_service;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final KafkaTemplate<String, ReviewEvent> kafkaTemplate;

    // Single constructor injecting both the database repository and the Kafka producer
    public ReviewController(ReviewRepository reviewRepository, KafkaTemplate<String, ReviewEvent> kafkaTemplate) {
        this.reviewRepository = reviewRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody Review review) {
        if (review.getRating() < 1 || review.getRating() > 5) {
            return ResponseEntity.badRequest().build();
        }
        
        // 1. Save the review to the MySQL database
        Review savedReview = reviewRepository.save(review);
        
        // 2. Create the event payload
        ReviewEvent event = new ReviewEvent(
            savedReview.getRecipeId(), 
            savedReview.getReviewerName(), 
            savedReview.getRating()
        );
        
        // 3. Publish the event to the Kafka "review-event" topic
        kafkaTemplate.send("review-event", String.valueOf(savedReview.getRecipeId()), event);
        
        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/recipe/{recipeId}")
    public List<Review> getReviewsByRecipe(@PathVariable Long recipeId) {
        return reviewRepository.findByRecipeIdOrderByCreatedAtDesc(recipeId);
    }

    @GetMapping("/recipe/{recipeId}/average")
    public double getAverageRating(@PathVariable Long recipeId) {
        List<Review> reviews = reviewRepository.findByRecipeIdOrderByCreatedAtDesc(recipeId);
        if (reviews.isEmpty()) return 0.0;
        double sum = reviews.stream().mapToInt(Review::getRating).sum();
        return Math.round((sum / reviews.size()) * 10.0) / 10.0;
    }
}