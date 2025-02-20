package githubio.ekomuliyo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "response_id")
    private UUID responseId;

    @NotBlank(message = "Medication name cannot be empty")
    private String name;

    @NotBlank(message = "Medication SKU cannot be empty")
    private String sku;

    private String image;
    
    private String description;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    private String manufacturer;

    private String responseMessage;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id")
    private List<MedicationIngredient> medicationIngredients;
}
