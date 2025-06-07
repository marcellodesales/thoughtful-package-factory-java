package ai.thoughtful.platform.factory.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST API controller for package classification and stack assignment.
 * Provides HTTP endpoints to classify packages and determine stack types.
 */
@RestController
@RequestMapping("/api/v1/packages")
@CrossOrigin(origins = "*")
public class ApiInfoController {

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
}

