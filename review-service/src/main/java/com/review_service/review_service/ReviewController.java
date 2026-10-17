package com.review_service.review_service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody Review review) {
        if (review.getRating() < 1 || review.getRating() > 5) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reviewRepository.save(review));
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