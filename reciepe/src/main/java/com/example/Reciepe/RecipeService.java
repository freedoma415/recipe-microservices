package com.example.Reciepe;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    // Map Entity to Response DTO
    private RecipeResponse mapToResponse(Recipe recipe) {
        return new RecipeResponse(recipe.getId(), recipe.getName(), recipe.getIngredients(), recipe.getDescription());
    }

    @CircuitBreaker(name = "recipeService", fallbackMethod = "getAllRecipesFallback")
    public List<RecipeResponse> getAllRecipes() {
        return recipeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Fallback method for getAllRecipes
    public List<RecipeResponse> getAllRecipesFallback(Throwable t) {
        System.err.println("CIRCUIT BREAKER OPEN: Fallback triggered for getAllRecipes because: " + t.getMessage());
        return List.of(); // Returns an empty list safely instead of failing
    }

    @CircuitBreaker(name = "recipeService", fallbackMethod = "getRecipeByIdFallback")
    public RecipeResponse getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        return mapToResponse(recipe);
    }

    // Fallback method for getRecipeById
    public RecipeResponse getRecipeByIdFallback(Long id, Throwable t) {
        System.err.println("CIRCUIT BREAKER OPEN: Fallback for getRecipeById (ID: " + id + "). Reason: " + t.getMessage());
        return new RecipeResponse(id, "Fallback Recipe", "N/A", "Service is temporarily unavailable.");
    }

    public RecipeResponse save(RecipeRequest request) {
        Recipe recipe = new Recipe();
        recipe.setName(request.name());
        recipe.setIngredients(request.ingredients());
        recipe.setDescription(request.description());
        
        Recipe savedRecipe = recipeRepository.save(recipe);
        return mapToResponse(savedRecipe);
    }

    public RecipeResponse putRecipe(Long id, RecipeRequest request) {
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id " + id));
        
        existingRecipe.setName(request.name());
        existingRecipe.setIngredients(request.ingredients());
        existingRecipe.setDescription(request.description());
        
        Recipe updatedRecipe = recipeRepository.save(existingRecipe);
        return mapToResponse(updatedRecipe);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }
}