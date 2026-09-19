package com.example.Reciepe;

public record RecipeResponse(
    Long id, 
    String name, 
    String ingredients, 
    String description
) {}