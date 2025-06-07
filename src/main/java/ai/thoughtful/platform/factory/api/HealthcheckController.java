package ai.thoughtful.platform.factory.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API controller for package classification and stack assignment.
 * Provides HTTP endpoints to classify packages and determine stack types.
 */
@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class HealthcheckController {

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

