package githubio.ekomuliyo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class MedicationIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isActive;
    private String name;
    private String sku;
    private String denominatorCode;
    private Integer denominatorValue;
    private String numeratorCode;
    private Integer numeratorValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id")
    private Medication medication;
}