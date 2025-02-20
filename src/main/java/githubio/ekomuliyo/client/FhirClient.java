package githubio.ekomuliyo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import githubio.ekomuliyo.dto.request.FhirMedicationRequest;
import githubio.ekomuliyo.dto.response.FhirMedicationResponse;

@FeignClient(name = "fhirClient", url = "https://api-satusehat-stg.dto.kemkes.go.id/fhir-r4/v1")
public interface FhirClient {

    @PostMapping("/Medication")
    FhirMedicationResponse createMedication(
        @RequestHeader("Authorization") String authorization,
        @RequestBody FhirMedicationRequest request
    );
}