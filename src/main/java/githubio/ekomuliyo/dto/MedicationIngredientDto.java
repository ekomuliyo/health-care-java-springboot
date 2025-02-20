package githubio.ekomuliyo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MedicationIngredientDto {
    @Schema(hidden = true)
    private Long id;
    private boolean isActive;
    private String name;
    private String sku;
    private String denominatorCode;
    private Integer denominatorValue;
    private String numeratorCode;
    private Integer numeratorValue;
}