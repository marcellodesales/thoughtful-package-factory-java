package ai.thoughtful.platform.factory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application for package classification command-line interface.
 */
@SpringBootApplication
public class PackageSorterApplication {

    public static void main(String[] args) {
        SpringApplication.run(PackageSorterApplication.class, args);
    }
}

