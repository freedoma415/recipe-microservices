package com.example.Reciepe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RecipeRequest(
    @NotBlank(message = "Recipe name is required")
    String name,
    
    @NotBlank(message = "Ingredients cannot be empty")
    String ingredients,
    
    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters long")
    String description
) {}