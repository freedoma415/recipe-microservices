package com.example.notification_service;

// Create this as a new file: ReviewEvent.java
public record ReviewEvent(Long recipeId, String reviewerName, int rating) {}