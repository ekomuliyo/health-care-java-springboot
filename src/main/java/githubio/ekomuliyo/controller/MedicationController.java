package githubio.ekomuliyo.controller;

import githubio.ekomuliyo.dto.MedicationDto;
import githubio.ekomuliyo.service.MedicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/medication")
@RequiredArgsConstructor
@Tag(name = "Medication", description = "Medication management APIs")
public class MedicationController {
    
    private final MedicationService medicationService;

    @Operation(
        summary = "Create a new medication",
        description = "Creates a new medication with the provided information and image"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Medication created successfully",
            content = @Content(schema = @Schema(implementation = MedicationDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content
        )
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(ref = "#/components/requestBodies/MedicationRequest")
    public ResponseEntity<MedicationDto> createMedication(
        @RequestPart(value = "image") MultipartFile image,
        @RequestPart(value = "medication") String medicationJson
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            MedicationDto medicationDto = mapper.readValue(medicationJson, MedicationDto.class);
            return ResponseEntity.ok(medicationService.createMedication(medicationDto, image));
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid medication JSON format: " + e.getMessage());
        }
    }

    @Operation(
        summary = "Get medication by ID",
        description = "Retrieves a medication by its ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Medication found",
            content = @Content(schema = @Schema(implementation = MedicationDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Medication not found",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicationDto> getMedicationById(
        @Parameter(description = "Medication ID", example = "1", schema = @Schema(type = "integer"))
        @PathVariable(value = "id") Long id
    ) {
        return ResponseEntity.ok(medicationService.getMedicationById(id));
    }

    @Operation(
        summary = "Update medication",
        description = "Updates an existing medication with new information and optional new image"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Medication updated successfully",
            content = @Content(schema = @Schema(implementation = MedicationDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Medication not found",
            content = @Content
        )
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(ref = "#/components/requestBodies/MedicationRequest")
    public ResponseEntity<MedicationDto> updateMedication(
        @PathVariable Long id,
        @RequestPart(value = "image", required = false) MultipartFile image,
        @RequestPart(value = "medication") String medicationJson
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            MedicationDto medicationDto = mapper.readValue(medicationJson, MedicationDto.class);
            return ResponseEntity.ok(medicationService.updateMedication(id, medicationDto, image));
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid medication JSON format: " + e.getMessage());
        }
    }

    @Operation(
        summary = "Delete medication",
        description = "Deletes a medication by its ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Medication deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Medication not found",
            content = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(
        @Parameter(description = "Medication ID", example = "1", schema = @Schema(type = "integer"))
        @PathVariable(value = "id") Long id
    ) {
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get all medications with pagination",
        description = "Retrieves a paginated list of medications with optional sorting"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Medications retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class))
        )
    })
    @GetMapping
    public ResponseEntity<Page<MedicationDto>> getAllMedications(
        @Parameter(description = "Page number (0-based)", example = "0", schema = @Schema(type = "integer"))
        @RequestParam(value = "page", defaultValue = "0") int page,
        @Parameter(description = "Number of items per page", example = "10", schema = @Schema(type = "integer"))
        @RequestParam(value = "size", defaultValue = "10") int size,
        @Parameter(description = "Field to sort by", example = "id", schema = @Schema(type = "string"))
        @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
        @Parameter(description = "Sort direction (asc/desc)", example = "asc", schema = @Schema(type = "string"))
        @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return ResponseEntity.ok(medicationService.getAllMedicationsWithPagination(pageable));
    }
}