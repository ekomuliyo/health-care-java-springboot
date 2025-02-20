package githubio.ekomuliyo.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class FhirMedicationResponse {
    private CodeableConcept code;
    private List<Extension> extension;
    private CodeableConcept form;
    private String id;
    private List<Identifier> identifier;
    private List<Ingredient> ingredient;
    private Reference manufacturer;
    private Meta meta;
    private String resourceType;
    private String status;

    @Data
    public static class CodeableConcept {
        private List<Coding> coding;
    }

    @Data
    public static class Coding {
        private String code;
        private String display;
        private String system;
    }

    @Data
    public static class Extension {
        private String url;
        private CodeableConcept valueCodeableConcept;
    }

    @Data
    public static class Identifier {
        private String system;
        private String use;
        private String value;
    }

    @Data
    public static class Ingredient {
        private boolean isActive;
        private CodeableConcept itemCodeableConcept;
        private Strength strength;
    }

    @Data
    public static class Strength {
        private Quantity denominator;
        private Quantity numerator;
    }

    @Data
    public static class Quantity {
        private String code;
        private String system;
        private double value;
    }

    @Data
    public static class Reference {
        private String reference;
    }

    @Data
    public static class Meta {
        private String lastUpdated;
        private List<String> profile;
        private String versionId;
    }
}
