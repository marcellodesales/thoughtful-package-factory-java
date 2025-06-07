package ai.thoughtful.platform.factory.api.dto;

import ai.thoughtful.platform.factory.algorithm.PackageClassificationType;
import ai.thoughtful.platform.factory.algorithm.StackType;
import ai.thoughtful.platform.factory.model.Package;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

/**
 * Data Transfer Object for package classification responses.
 * Contains the classification result and optional package details.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OperationResponse(
    Package packageInfo,
    Set<PackageClassificationType> classification,
    StackType selectedStack,
    Set<String> remarks,
    String error,
    Long timestamp
) {

    /**
     * Success response constructor.
     */
    public OperationResponse(Package packageInfo, Set<PackageClassificationType> classification, StackType selectedStack, Set<String> remarks) {
        this(packageInfo, classification, selectedStack, remarks, null, System.currentTimeMillis());
    }
    
    /**
     * Error response constructor.
     */
    public OperationResponse(String error) {
        this(null, null, null, null, error, System.currentTimeMillis());
    }

}

