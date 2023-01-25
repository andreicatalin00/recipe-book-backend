package recipes.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Email
    @Pattern(regexp = ".+@.+\\..+")
    @NotBlank
    @NotNull
    private String email;

    @Length(min = 8, message = "Should have at least 8 characters")
    @NotNull
    @NotBlank
    private String password;

    @OneToMany
    @JsonManagedReference
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Recipe> recipes = new ArrayList<>();
}
