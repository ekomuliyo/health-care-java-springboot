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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medication")
@RequiredArgsConstructor
@Tag(name = "Medication", description = "Medication management APIs")
public class MedicationController {
    
    private final MedicationService medicationService;

    @Operation(
        summary = "Create a new medication",
        description = "Creates a new medication with the provided information"
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
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<MedicationDto> createMedication(@RequestBody MedicationDto medicationDto) {
        return ResponseEntity.ok(medicationService.createMedication(medicationDto));
    }
}