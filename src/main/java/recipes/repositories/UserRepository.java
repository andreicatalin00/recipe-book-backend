package recipes.repositories;

import org.springframework.data.repository.CrudRepository;
import recipes.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
}
