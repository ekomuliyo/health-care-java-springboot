package githubio.ekomuliyo.dto.request;

import lombok.Builder;
import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
public class FhirMedicationRequest {
    private String resourceType;
    private Meta meta;
    private List<Identifier> identifier;
    private CodeableConcept code;
    private String status;
    private Reference manufacturer;
    private CodeableConcept form;
    private List<Ingredient> ingredient;
    private List<Extension> extension;

    // Add nested classes for the complex structure
    @Data
    @Builder
    public static class Meta {
        private List<String> profile;
    }

    @Data
    @Builder
    public static class Identifier {
        private String system;
        private String use;
        private String value;
    }

    @Data
    @Builder
    public static class CodeableConcept {
        private List<Coding> coding;
    }

    @Data
    @Builder
    public static class Coding {
        private String system;
        private String code;
        private String display;
    }

    @Data
    @Builder
    public static class Reference {
        private String reference;
    }

    @Data
    @Builder
    public static class Ingredient {
        private CodeableConcept itemCodeableConcept;

        @JsonProperty("isActive")
        private boolean isActive;
        private Strength strength;
    }

    @Data
    @Builder
    public static class Strength {
        private Quantity numerator;
        private Quantity denominator;
    }

    @Data
    @Builder
    public static class Quantity {
        private double value;
        private String system;
        private String code;
    }

    @Data
    @Builder
    public static class Extension {
        private String url;
        private CodeableConcept valueCodeableConcept;
    }
}