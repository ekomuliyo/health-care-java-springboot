package githubio.ekomuliyo.dto;

import lombok.Data;

@Data
public class MedicationIngredientDto {
    private Long id;
    private boolean isActive;
    private String name;
    private String sku;
    private String denominatorCode;
    private Integer denominatorValue;
    private String numeratorCode;
    private Integer numeratorValue;
}