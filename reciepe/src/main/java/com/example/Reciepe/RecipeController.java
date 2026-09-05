package com.example.Reciepe;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeEventProducer eventProducer; // Inject the Kafka producer

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/id/{id}")
    public Recipe getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }


    @PostMapping
    public ResponseEntity<?> createRecipe(@RequestBody Recipe recipe) {
        try {
            // 1. Save to the Database
            Recipe savedRecipe = recipeService.createRecipe(recipe);
            
            // 2. Fire the Kafka Event
            eventProducer.sendRecipeCreatedEvent(savedRecipe.getName());
            
            // 3. Return a 201 Created status with the recipe data
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
            
        } catch (Exception e) {
            // 4. Catch any DB or Kafka errors and return a 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create recipe: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public Recipe putRecipe(@PathVariable Long id, @RequestBody Recipe recipeDetails) {
        return recipeService.putRecipe(id, recipeDetails);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
    }
}