package githubio.ekomuliyo.exception;

import lombok.Getter;

@Getter
public class FhirApiException extends RuntimeException {
    private final int statusCode;
    
    public FhirApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}