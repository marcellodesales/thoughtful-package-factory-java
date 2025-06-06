package ai.thoughtful.platform.factory.api.dto;

/**
 * Data Transfer Object for package classification requests.
 * Contains the package dimensions and mass required for classification.
 */
public record ClassificationRequest(
    int width,
    int height, 
    int length,
    double mass
) {

}

