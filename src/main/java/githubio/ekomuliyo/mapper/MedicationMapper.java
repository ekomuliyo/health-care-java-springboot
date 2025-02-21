package githubio.ekomuliyo.mapper;

import githubio.ekomuliyo.dto.MedicationDto;
import githubio.ekomuliyo.dto.MedicationIngredientDto;
import githubio.ekomuliyo.entity.Medication;
import githubio.ekomuliyo.entity.MedicationIngredient;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MedicationMapper {
    
    public Medication toEntity(MedicationDto dto) {
        Medication entity = new Medication();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setSku(dto.getSku());
        entity.setImage(dto.getImage());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setManufacturer(dto.getManufacturer());
        
        if (dto.getMedicationIngredients() != null) {
            entity.setMedicationIngredients(
                dto.getMedicationIngredients().stream()
                    .map(this::toIngredientEntity)
                    .collect(Collectors.toList())
            );
        }
        return entity;
    }

    public MedicationDto toDto(Medication entity) {
        MedicationDto dto = new MedicationDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSku(entity.getSku());
        dto.setImage(entity.getImage());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setManufacturer(entity.getManufacturer());
        dto.setResponseId(entity.getResponseId() != null ? entity.getResponseId().toString() : null);
        
        if (entity.getMedicationIngredients() != null) {
            dto.setMedicationIngredients(
                entity.getMedicationIngredients().stream()
                    .map(this::toIngredientDto)
                    .collect(Collectors.toList())
            );
        }
        return dto;
    }

    private MedicationIngredient toIngredientEntity(MedicationIngredientDto dto) {
        MedicationIngredient entity = new MedicationIngredient();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setSku(dto.getSku());
        entity.setActive(dto.isActive());
        entity.setDenominatorCode(dto.getDenominatorCode());
        entity.setDenominatorValue(dto.getDenominatorValue());
        entity.setNumeratorCode(dto.getNumeratorCode());
        entity.setNumeratorValue(dto.getNumeratorValue());
        return entity;
    }

    private MedicationIngredientDto toIngredientDto(MedicationIngredient entity) {
        MedicationIngredientDto dto = new MedicationIngredientDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSku(entity.getSku());
        dto.setActive(entity.isActive());
        dto.setDenominatorCode(entity.getDenominatorCode());
        dto.setDenominatorValue(entity.getDenominatorValue());
        dto.setNumeratorCode(entity.getNumeratorCode());
        dto.setNumeratorValue(entity.getNumeratorValue());
        return dto;
    }
}