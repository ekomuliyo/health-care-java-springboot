package githubio.ekomuliyo.dto;

import lombok.Data;
import java.util.List;

@Data
public class MedicationDto {
    private Long id;
    private String name;
    private String sku;
    private String image;
    private String description;
    private Double price;
    private String manufacturer;
    private List<MedicationIngredientDto> medicationIngredients;
}
