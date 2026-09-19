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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeEventProducer eventProducer; 

    @GetMapping
    public List<RecipeResponse> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getRecipeById(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getRecipeById(id));
    }

    @PostMapping
    public ResponseEntity<String> createRecipe(@Valid @RequestBody RecipeRequest request) {
        RecipeResponse saved = recipeService.save(request);
        // Optionally trigger Kafka event here if needed
        return ResponseEntity.status(HttpStatus.CREATED).body("Recipe successfully added!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateRecipe(@PathVariable Long id, @Valid @RequestBody RecipeRequest request) {
        recipeService.putRecipe(id, request);
        return ResponseEntity.ok("Recipe successfully updated!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.ok("Recipe successfully deleted!");
    }
}

// @PostMapping
    // public ResponseEntity<RecipeResponse> createRecipe(@Valid @RequestBody RecipeRequest request) {
    //     // Fixed type mismatch: Service now returns RecipeResponse
    //     RecipeResponse response = recipeService.createRecipe(request);
        
    //     // Fire Kafka event (Update this method call if your producer uses a different method name)
    //     eventProducer.sendMessage("New recipe created: " + response.name());
        
    //     return ResponseEntity.status(HttpStatus.CREATED).body(response);
    // }