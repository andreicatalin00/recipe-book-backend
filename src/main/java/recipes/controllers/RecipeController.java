package recipes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import recipes.models.Recipe;
import recipes.models.User;
import recipes.services.interfaces.RecipeService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class RecipeController {
    RecipeService recipeService;
    PasswordEncoder passwordEncoder;

    @Autowired
    public RecipeController(RecipeService recipeService, PasswordEncoder passwordEncoder) {
        this.recipeService = recipeService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<User> postUser(@Valid @RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = recipeService.registerUser(user);

        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @PostMapping("/recipe/new")
    public ResponseEntity<Object> postRecipe(@Valid @RequestBody Recipe recipe) {
        Recipe newRecipe = recipeService.save(recipe);
        return new ResponseEntity<>(Collections.singletonMap("id", newRecipe.getId()), HttpStatus.OK);
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable long id) {
        return new ResponseEntity<>(recipeService.findRecipeById(id), HttpStatus.OK);
    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable long id, @Valid @RequestBody Recipe recipe) {
        this.recipeService.updateRecipeById(id, recipe);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/recipe/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable long id) {
        recipeService.deleteRecipeById(id);
    }

    @GetMapping("/recipe/search")
    public ResponseEntity<Collection<Recipe>> searchRecipe(@RequestParam Map<String, String> allParams) {
        if (allParams.size() != 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (allParams.containsKey("category")) {
            return new ResponseEntity<>(recipeService.findRecipesByCategory(allParams.get("category")), HttpStatus.OK);
        } else if (allParams.containsKey("name")) {
            return new ResponseEntity<>(recipeService.findRecipesByName(allParams.get("name")), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
