package com.example.Reciepe;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    // Map Entity to Response DTO
    private RecipeResponse mapToResponse(Recipe recipe) {
        return new RecipeResponse(recipe.getId(), recipe.getName(), recipe.getIngredients(), recipe.getDescription());
    }

    public List<RecipeResponse> getAllRecipes() {
        return recipeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RecipeResponse getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        return mapToResponse(recipe);
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