package ai.thoughtful.platform.factory.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Data Transfer Object for package classification responses.
 * Contains the classification result and optional package details.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClassificationResponse(
    Integer width,
    Integer height,
    Integer length,
    Double mass,
    String classification,
    String error,
    Long timestamp,
    PackageDetails details
) {
    
    /**
     * Success response constructor.
     */
    public ClassificationResponse(int width, int height, int length, double mass, String classification) {
        this(width, height, length, mass, classification, null, System.currentTimeMillis(),
             new PackageDetails(width, height, length, mass));
    }
    
    /**
     * Error response constructor.
     */
    public ClassificationResponse(String error) {
        this(null, null, null, null, null, error, System.currentTimeMillis(), null);
    }
    
    /**
     * Package details for additional information.
     */
    public record PackageDetails(
        long volume,
        boolean isBulky,
        boolean isHeavy,
        String bulkyReason,
        String classification
    ) {
        
        public PackageDetails(int width, int height, int length, double mass) {
            this(
                calculateVolume(width, height, length),
                isBulky(width, height, length),
                isHeavy(mass),
                getBulkyReason(width, height, length),
                getClassification(width, height, length, mass)
            );
        }

        private static long calculateVolume(int width, int height, int length) {
            return (long) width * height * length;
        }

        private static boolean isBulky(int width, int height, int length) {
            return width >= 150 || height >= 150 || length >= 150 || calculateVolume(width, height, length) >= 1_000_000;
        }

        private static boolean isHeavy(double mass) {
            return mass >= 20_000;
        }

        private static String getBulkyReason(int width, int height, int length) {
            if (width >= 150 || height >= 150 || length >= 150) {
                return "Dimension >= 150cm";
            }
            if (calculateVolume(width, height, length) >= 1_000_000) {
                return "Volume >= 1,000,000 cm³";
            }
            return null;
        }
        
        private static String getClassification(int width, int height, int length, double mass) {
            boolean bulky = isBulky(width, height, length);
            boolean heavy = isHeavy(mass);
            
            if (bulky && heavy) {
                return "BULKY and HEAVY";
            } else if (bulky) {
                return "BULKY only";
            } else if (heavy) {
                return "HEAVY only";
            } else {
                return "STANDARD";
            }
        }
    }
}

