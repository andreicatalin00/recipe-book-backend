package recipes.services.interfaces;

import recipes.models.Recipe;
import recipes.models.User;

import java.util.Collection;
import java.util.Optional;

public interface RecipeService {
    Recipe save(Recipe recipe);
    void updateRecipeById(Long id, Recipe recipe);
    Recipe findRecipeById(Long id);
    void deleteRecipeById(Long id);
    Collection<Recipe> findRecipesByCategory(String category);
    Collection<Recipe> findRecipesByName(String name);
    User registerUser(User user);
    Iterable<User> findAllUsers();

    Optional<User> findUserById(Long id);
}
