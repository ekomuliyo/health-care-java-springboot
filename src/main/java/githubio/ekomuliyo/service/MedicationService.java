package githubio.ekomuliyo.service;

import githubio.ekomuliyo.dto.MedicationDto;
import githubio.ekomuliyo.dto.TokenResponse;
import githubio.ekomuliyo.dto.request.FhirMedicationRequest;
import githubio.ekomuliyo.dto.response.FhirMedicationResponse;
import githubio.ekomuliyo.mapper.MedicationMapper;
import githubio.ekomuliyo.repository.MedicationRepository;
import githubio.ekomuliyo.entity.Medication;
import githubio.ekomuliyo.storage.StorageService;
import githubio.ekomuliyo.exception.ResourceNotFoundException;
import githubio.ekomuliyo.exception.ValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import githubio.ekomuliyo.client.FhirClient;
import githubio.ekomuliyo.service.AuthService;
import java.util.UUID;
import githubio.ekomuliyo.exception.FhirApiException;

@Service
@Validated
@RequiredArgsConstructor
public class MedicationService {
    
    private final MedicationRepository medicationRepository;
    private final MedicationMapper medicationMapper;
    private final StorageService storageService;
    private final FhirClient fhirClient;
    private final AuthService authService;

    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        propagation = Propagation.REQUIRED,
        rollbackFor = Exception.class
    )
    public MedicationDto createMedication(@Valid MedicationDto medicationDto, MultipartFile image) {
        try {
            // Get access token

            TokenResponse tokenResponse = authService.getCredentials();
            String accessToken = tokenResponse.getAccessToken();
            String orgId = tokenResponse.getOrgId();

            
            // Create FHIR medication request
            FhirMedicationRequest fhirRequest = FhirMedicationRequest.builder()
                .resourceType("Medication")
                .meta(FhirMedicationRequest.Meta.builder()
                    .profile(List.of("https://fhir.kemkes.go.id/r4/StructureDefinition/Medication"))
                    .build())
                .identifier(List.of(FhirMedicationRequest.Identifier.builder()
                    .system(String.format("http://sys-ids.kemkes.go.id/medication/%s", orgId))
                    .value("1234567890")
                    .build()))
                .code(FhirMedicationRequest.CodeableConcept.builder()
                    .coding(List.of(FhirMedicationRequest.Coding.builder()
                        .system("http://sys-ids.kemkes.go.id/kfa")
                        .code(medicationDto.getSku())
                        .display(medicationDto.getName())
                        .build()))
                    .build())
                .status("active")
                .manufacturer(FhirMedicationRequest.Reference.builder()
                    .reference(String.format("Organization/%s", orgId))
                    .build())
                .form(FhirMedicationRequest.CodeableConcept.builder()
                    .coding(List.of(FhirMedicationRequest.Coding.builder()
                        .system("http://terminology.kemkes.go.id/CodeSystem/medication-form")
                        .code("BS023")
                        .display("Kaplet Salut Selaput")
                        .build()))
                    .build())
                .ingredient(medicationDto.getMedicationIngredients().stream()
                    .map(ingredient -> FhirMedicationRequest.Ingredient.builder()
                        .itemCodeableConcept(FhirMedicationRequest.CodeableConcept.builder()
                            .coding(List.of(FhirMedicationRequest.Coding.builder()
                                .system("http://sys-ids.kemkes.go.id/kfa")
                                .code(ingredient.getSku())
                                .display(ingredient.getName())
                                .build()))
                            .build())
                        .isActive(ingredient.isActive())
                        .strength(FhirMedicationRequest.Strength.builder()
                            .numerator(FhirMedicationRequest.Quantity.builder()
                                .value(ingredient.getNumeratorValue())
                                .system("http://unitsofmeasure.org")
                                .code(ingredient.getNumeratorCode())
                                .build())
                            .denominator(FhirMedicationRequest.Quantity.builder()
                                .value(ingredient.getDenominatorValue())
                                .system("http://terminology.hl7.org/CodeSystem/v3-orderableDrugForm")
                                .code(ingredient.getDenominatorCode())
                                .build())
                            .build())
                        .build())
                    .collect(Collectors.toList()))
                .extension(List.of(FhirMedicationRequest.Extension.builder()
                    .url("https://fhir.kemkes.go.id/r4/StructureDefinition/MedicationType")
                    .valueCodeableConcept(FhirMedicationRequest.CodeableConcept.builder()
                        .coding(List.of(FhirMedicationRequest.Coding.builder()
                            .system("http://terminology.kemkes.go.id/CodeSystem/medication-type")
                            .code("NC")
                            .display("Non-compound")
                            .build()))
                        .build())
                    .build()))
                .build();

            // Call FHIR API
            FhirMedicationResponse fhirResponse;
            try {
                fhirResponse = fhirClient.createMedication("Bearer " + accessToken, fhirRequest);
            } catch (feign.FeignException e) {
                throw new FhirApiException(e.contentUTF8(), e.status());
            }
            
            // Validate image
            if (image == null || image.isEmpty()) {
                throw new ValidationException("Image file is required");
            }

            // Handle image upload
            String imagePath = storageService.store(image);
            medicationDto.setImage(imagePath);
            
            // Convert and save
            Medication medication = medicationMapper.toEntity(medicationDto);
            medication.setResponseId(UUID.fromString(fhirResponse.getId())); // Convert String to UUID
            Medication savedMedication = medicationRepository.save(medication);
            return medicationMapper.toDto(savedMedication);
        } catch (jakarta.validation.ConstraintViolationException e) {
            String message = e.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.joining(", "));
            throw new ValidationException(message);
        } catch (Exception e) {
            if (e instanceof ValidationException) {
                throw e;
            }
            throw new RuntimeException("Failed to create medication: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public MedicationDto getMedicationById(Long id) {
        Medication medication = medicationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medication not found with id: " + id));
        return medicationMapper.toDto(medication);
    }

    @Transactional(readOnly = true)
    public List<MedicationDto> getAllMedications() {
        List<Medication> medications = medicationRepository.findAll();
        return medications.stream()
                .map(medicationMapper::toDto)
                .toList();
    }

    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        propagation = Propagation.REQUIRED,
        rollbackFor = Exception.class
    )
    public MedicationDto updateMedication(Long id, @Valid MedicationDto medicationDto, MultipartFile image) {
        try {
            Medication existingMedication = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found with id: " + id));

            // Handle image upload if new image is provided
            if (image != null && !image.isEmpty()) {
                String imagePath = storageService.store(image);
                medicationDto.setImage(imagePath);
            } else {
                // Keep existing image if no new image is provided
                medicationDto.setImage(existingMedication.getImage());
            }

            // Update entity fields
            medicationDto.setId(id); // Ensure ID is set for update
            Medication medication = medicationMapper.toEntity(medicationDto);
            Medication updatedMedication = medicationRepository.save(medication);
            
            return medicationMapper.toDto(updatedMedication);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update medication: " + e.getMessage(), e);
        }
    }

    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        propagation = Propagation.REQUIRED,
        rollbackFor = Exception.class
    )
    public void deleteMedication(Long id) {
        try {
            Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found with id: " + id));

            // Delete the associated image if it exists
            if (medication.getImage() != null) {
                // Extract filename from path
                String filename = medication.getImage().substring(medication.getImage().lastIndexOf('/') + 1);
                try {
                    storageService.deleteFile(filename);
                } catch (Exception e) {
                    // Log error but continue with medication deletion
                    System.err.println("Failed to delete image file: " + e.getMessage());
                }
            }

            medicationRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete medication: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return medicationRepository.existsByName(name);
    }

    @Transactional(readOnly = true)
    public boolean existsBySku(String sku) {
        return medicationRepository.existsBySku(sku);
    }
}