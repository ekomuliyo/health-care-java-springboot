package githubio.ekomuliyo.service;

import githubio.ekomuliyo.dto.MedicationDto;
import githubio.ekomuliyo.mapper.MedicationMapper;
import githubio.ekomuliyo.repository.MedicationRepository;
import githubio.ekomuliyo.entity.Medication;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class MedicationService {
    
    private final MedicationRepository medicationRepository;
    private final MedicationMapper medicationMapper;

    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        propagation = Propagation.REQUIRED,
        rollbackFor = Exception.class
    )
    public MedicationDto createMedication(@Valid MedicationDto medicationDto) {
        try {
            Medication medication = medicationMapper.toEntity(medicationDto);
            Medication savedMedication = medicationRepository.save(medication);
            return medicationMapper.toDto(savedMedication);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create medication: " + e.getMessage(), e);
        }
    }
}