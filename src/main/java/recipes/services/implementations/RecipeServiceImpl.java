package recipes.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import recipes.models.Recipe;
import recipes.models.User;
import recipes.repositories.RecipeRepository;
import recipes.repositories.UserRepository;
import recipes.services.interfaces.RecipeService;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Recipe save(Recipe newRecipe) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails details = (UserDetails) auth.getPrincipal();
        Optional<User> userOptional = userRepository.findUserByEmail(details.getUsername());
        userOptional.ifPresent(newRecipe::setUser);
        return recipeRepository.save(newRecipe);
    }

    @Override
    @Transactional
    public void updateRecipeById(Long id, Recipe newRecipe) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails details = (UserDetails) auth.getPrincipal();
        Recipe recipe = this.findRecipeById(id);
        if (recipe == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (recipe.getUser().getEmail().equals(details.getUsername())) {
            recipe.setCategory(newRecipe.getCategory());
            recipe.setDescription(newRecipe.getDescription());
            recipe.setName(newRecipe.getName());
            recipe.setDirections(newRecipe.getDirections());
            recipe.setIngredients(newRecipe.getIngredients());
            recipe.setDate(newRecipe.getDate());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

    }

    @Override
    public Recipe findRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found corresponding to the id  " + id));
    }

    @Override
    public void deleteRecipeById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails details = (UserDetails) auth.getPrincipal();
        Recipe recipe = findRecipeById(id);
        if (recipe.getUser().getEmail().equals(details.getUsername())) {
            recipeRepository.delete(recipe);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public Collection<Recipe> findRecipesByCategory(String category) {
        List<Recipe> recipeList = new LinkedList<>();
        recipeRepository.findAllByCategory(category).forEach(recipeList::add);
        sortByDateDescending(recipeList);
        return recipeList;
    }

    @Override
    public Collection<Recipe> findRecipesByName(String name) {
        List<Recipe> recipeList = new LinkedList<>();
        recipeRepository.findAllByNameIgnoreCase(name).forEach(recipeList::add);
        sortByDateDescending(recipeList);
        return recipeList;
    }

    @Override
    public User registerUser(User user) {
        boolean exists = userRepository.findUserByEmail(user.getEmail()).isPresent();
        if (exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email address exists already in the database");
        }
        return userRepository.save(user);
    }

    @Override
    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    private static void sortByDateDescending(List<Recipe> recipeList) {
        recipeList.sort((Recipe o1, Recipe o2) -> o1.getDate().compareTo(o2.getDate()) * (-1));
    }
}
