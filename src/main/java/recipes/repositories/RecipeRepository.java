package recipes.repositories;

import org.springframework.data.repository.CrudRepository;
import recipes.models.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}