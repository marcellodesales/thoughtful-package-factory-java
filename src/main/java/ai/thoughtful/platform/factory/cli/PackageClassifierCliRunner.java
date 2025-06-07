package ai.thoughtful.platform.factory.cli;

import ai.thoughtful.platform.factory.AutomationFactoryService;
import ai.thoughtful.platform.factory.model.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Command-line runner that processes package measurements and returns stack type.
 * 
 * Expected input format: "width,height,length,mass"
 * Example: "50,30,20,5000" -> "STANDARD"
 */
@Component
public class PackageClassifierCliRunner implements CommandLineRunner {

    @Autowired
    private AutomationFactoryService service;

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String input = args[0];
        
        if ("--help".equals(input) || "-h".equals(input)) {
            printHelp();
            return;
        }

        try {
            String stackType = this.sortPackage(input);
            System.out.println(stackType);

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            printUsage();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Classifies a package based on the input string format.
     * 
     * @param input comma-separated values: "width,height,length,mass"
     * @return stack type as string (STANDARD, SPECIAL, or REJECTED)
     * @throws IllegalArgumentException if input format is invalid
     */
    private String sortPackage(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }

        String[] parts = input.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException(
                "Input must have exactly 4 comma-separated values: width,height,length,mass");
        }

        int width = Integer.parseInt(parts[0].trim());
        int height = Integer.parseInt(parts[1].trim());
        int length = Integer.parseInt(parts[2].trim());
        double mass = Double.parseDouble(parts[3].trim());

        Package pkg = service.makeNewPackage(width, height, length, mass);

        // Use StackType.sort method to get classification
        return service.sortPackage(pkg).name();
    }

    private void printUsage() {
        System.out.println("Usage: java -jar package-classifier.jar \"width,height,length,mass\"");
        System.out.println("Example: java -jar package-classifier.jar \"50,30,20,5000\"");
        System.out.println("Use --help for more information.");
    }

    private void printHelp() {
        System.out.println("📦 Package Classification System");
        System.out.println("================================");
        System.out.println();
        System.out.println("This tool classifies packages and determines their stack assignment based on");
        System.out.println("dimensions and weight.");
        System.out.println();
        System.out.println("USAGE:");
        System.out.println("  java -jar package-classifier.jar \"width,height,length,mass\"");
        System.out.println();
        System.out.println("PARAMETERS:");
        System.out.println("  width  - Package width in centimeters (positive integer)");
        System.out.println("  height - Package height in centimeters (positive integer)");
        System.out.println("  length - Package length in centimeters (positive integer)");
        System.out.println("  mass   - Package mass in grams (positive number)");
        System.out.println();
        System.out.println("OUTPUT:");
        System.out.println("  STANDARD - Normal processing (not bulky, not heavy)");
        System.out.println("  SPECIAL  - Special handling (bulky OR heavy, but not both)");
        System.out.println("  REJECTED - Cannot process (both bulky AND heavy)");
        System.out.println();
        System.out.println("CLASSIFICATION RULES:");
        System.out.println("  • BULKY: Any dimension ≥ 150cm OR volume ≥ 1,000,000 cm³");
        System.out.println("  • HEAVY: Mass ≥ 20,000 grams (20 kg)");
        System.out.println();
        System.out.println("EXAMPLES:");
        System.out.println("  \"50,30,20,5000\"     -> STANDARD (normal size and weight)");
        System.out.println("  \"150,30,20,5000\"    -> SPECIAL  (bulky by dimension)");
        System.out.println("  \"50,30,20,25000\"    -> SPECIAL  (heavy package)");
        System.out.println("  \"150,30,20,25000\"   -> REJECTED (both bulky and heavy)");
        System.out.println("  \"100,100,100,15000\" -> SPECIAL  (bulky by volume: 1M cm³)");
    }
}

