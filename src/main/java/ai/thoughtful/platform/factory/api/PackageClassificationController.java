package ai.thoughtful.platform.factory.api;

import ai.thoughtful.platform.factory.AutomationFactoryService;
import ai.thoughtful.platform.factory.PackageClassification;
import ai.thoughtful.platform.factory.api.dto.ClassificationResponse;
import ai.thoughtful.platform.factory.model.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.EnumSet;

/**
 * REST API controller for package classification and stack assignment.
 * Provides HTTP endpoints to classify packages and determine stack types.
 */
@RestController
@RequestMapping("/api/v1/packages")
@CrossOrigin(origins = "*")
public class AutomationFactoryController {

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
            EnumSet<PackageClassification> packageClassifications = service.classifyPackage(pkg);

            // Create response
            ClassificationResponse response = new ClassificationResponse(
                width, height, length, mass, packageClassifications.toString()
            );
            
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

    /**
     * Get API information and usage examples.
     * 
     * @return API information
     */
    @GetMapping("/info")
    public ResponseEntity<Object> getApiInfo() {
        return ResponseEntity.ok(new Object() {
            public final String name = "Package Classification API";
            public final String version = "1.0.0";
            public final String description = "Classifies packages and assigns stack types based on dimensions and weight";
            public final Object endpoints = new Object() {
                public final String classify = "/api/v1/packages/classify?width={width}&height={height}&length={length}&mass={mass}";
                public final String classifyPath = "/api/v1/packages/classify/{width}/{height}/{length}/{mass}";
                public final String info = "/api/v1/packages/info";
                public final String health = "/api/v1/packages/health";
            };
            public final Object examples = new Object() {
                public final String standard = "/api/v1/packages/classify?width=50&height=30&length=20&mass=5000";
                public final String bulky = "/api/v1/packages/classify?width=150&height=30&length=20&mass=5000";
                public final String heavy = "/api/v1/packages/classify?width=50&height=30&length=20&mass=25000";
                public final String rejected = "/api/v1/packages/classify?width=150&height=30&length=20&mass=25000";
            };
            public final Object rules = new Object() {
                public final String bulky = "Any dimension >= 150cm OR volume >= 1,000,000 cm³";
                public final String heavy = "Mass >= 20,000 grams (20 kg)";
                public final String standard = "Neither bulky nor heavy";
                public final String special = "Bulky OR heavy (not both)";
                public final String rejected = "Both bulky AND heavy";
            };
        });
    }

    /**
     * Health check endpoint.
     * 
     * @return Health status
     */
    @GetMapping("/health")
    public ResponseEntity<Object> health() {
        return ResponseEntity.ok(new Object() {
            public final String status = "UP";
            public final long timestamp = System.currentTimeMillis();
            public final String service = "Package Classification API";
        });
    }
}

