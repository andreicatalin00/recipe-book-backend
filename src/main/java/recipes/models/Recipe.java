package recipes.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Category is mandatory")
    private String category;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Ingredients should NOT be null")
    @Size(min = 1, message = "Should have at least 1 ingredient")
    @ElementCollection
    private List<String> ingredients;

    @NotNull(message = "Directions shouldn't be null")
    @Size(min = 1, message = "Should have at least 1 direction")
    @ElementCollection
    private List<String> directions;
    @UpdateTimestamp
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;
}