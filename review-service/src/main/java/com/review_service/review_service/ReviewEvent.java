package com.review_service.review_service;

// Create this as a new file: ReviewEvent.java
public record ReviewEvent(Long recipeId, String reviewerName, int rating) {}