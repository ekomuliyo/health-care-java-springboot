package githubio.ekomuliyo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
public class MedicationDto {
    @Schema(hidden = true)
    private Long id;
    private String responseId;
    private String responseMessage;
    private String name;
    private String sku;
    private String image;
    private String description;
    private Double price;
    private String manufacturer;
    private List<MedicationIngredientDto> medicationIngredients;
}
