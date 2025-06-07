package ai.thoughtful.platform.factory;

import ai.thoughtful.platform.factory.cli.PackageClassifierCliRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application for package classification.
 * Supports both CLI and REST API modes.
 */
@SpringBootApplication
public class PackageSorterApplication {

    public static void main(String[] args) {
        // Check if running as CLI or API server
        if (args.length > 0 && !args[0].startsWith("--")) {
            // CLI mode - run classification and exit
            SpringApplication.run(PackageClassifierCliRunner.class, args);

        } else {
            // API server mode - start Spring Boot web server
            SpringApplication.run(PackageSorterApplication.class, args);
        }
    }
}

