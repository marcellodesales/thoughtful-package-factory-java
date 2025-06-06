package ai.thoughtful.platform.factory.api.dto;

import ai.thoughtful.platform.factory.algorithm.PackageClassificationType;
import ai.thoughtful.platform.factory.model.Package;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

/**
 * Data Transfer Object for package classification responses.
 * Contains the classification result and optional package details.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClassificationResponse(
    Package packageInfo,
    Set<PackageClassificationType> classification,
    Set<String> remarks,
    String error,
    Long timestamp
) {
    
    /**
     * Success response constructor.
     */
    public ClassificationResponse(Package packageInfo, Set<PackageClassificationType> classification, Set<String> remarks) {
        this(packageInfo, classification, remarks, null, System.currentTimeMillis());
    }
    
    /**
     * Error response constructor.
     */
    public ClassificationResponse(String error) {
        this(null, null, null, error, System.currentTimeMillis());
    }

}

