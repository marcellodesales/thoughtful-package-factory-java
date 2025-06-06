package ai.thoughtful.platform.factory.api;

import ai.thoughtful.platform.factory.AutomationFactoryService;
import ai.thoughtful.platform.factory.algorithm.PackageClassificationType;
import ai.thoughtful.platform.factory.api.dto.ClassificationResponse;
import ai.thoughtful.platform.factory.model.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * REST API controller for package classification and stack assignment.
 * Provides HTTP endpoints to classify packages and determine stack types.
 */
@RestController
@RequestMapping("/api/v1/packages")
@CrossOrigin(origins = "*")
public class PackageClassificationController {

    @Autowired
    private AutomationFactoryService service;

    /**
     * Classify a package using query parameters.
     * 
     * @param width Package width in centimeters
     * @param height Package height in centimeters
     * @param length Package length in centimeters
     * @param mass Package mass in grams
     * @return Classification response with stack assignment
     */
    @GetMapping("/classify")
    public ResponseEntity<ClassificationResponse> classifyPackage(
            @RequestParam("width") int width,
            @RequestParam("height") int height,
            @RequestParam("length") int length,
            @RequestParam("mass") double mass) {
        
        try {
            Package pkg = service.makeNewPackage(width, height, length, mass);

            // Perform classification
            EnumSet<PackageClassificationType> packageClassifications = service.classifyPackage(pkg);
            // Get the remarks about it
            Set<String> classificationRemarks = service.getClassificationReason(pkg, packageClassifications);

            // Create response
            // Transform classification
            Set<PackageClassificationType> classify = new HashSet<>(packageClassifications);
            ClassificationResponse response = new ClassificationResponse(pkg, classify, classificationRemarks);
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ClassificationResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ClassificationResponse("Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Classify a package using path variables for a more RESTful approach.
     * 
     * @param width Package width in centimeters
     * @param height Package height in centimeters
     * @param length Package length in centimeters
     * @param mass Package mass in grams
     * @return Classification response with stack assignment
     */
    @GetMapping("/classify/{width}/{height}/{length}/{mass}")
    public ResponseEntity<ClassificationResponse> classifyPackageByPath(
            @PathVariable("width") int width,
            @PathVariable("height") int height,
            @PathVariable("length") int length,
            @PathVariable("mass") double mass) {
        
        // Delegate to the main classification method
        return classifyPackage(width, height, length, mass);
    }

}

