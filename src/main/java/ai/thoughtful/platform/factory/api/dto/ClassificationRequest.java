package ai.thoughtful.platform.factory.api.dto;

/**
 * Data Transfer Object for package classification requests.
 * Contains the package dimensions and mass required for classification.
 */
public record ClassificationRequest(
    int width,
    int height, 
    int length,
    double mass
) {
    
    /**
     * Validates that all dimensions and mass are positive.
     * 
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public void validate() {
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be positive, got: " + width);
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive, got: " + height);
        }
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive, got: " + length);
        }
        if (mass <= 0) {
            throw new IllegalArgumentException("Mass must be positive, got: " + mass);
        }
        if (!Double.isFinite(mass)) {
            throw new IllegalArgumentException("Mass must be a finite number, got: " + mass);
        }
    }
    
    /**
     * Calculates the volume of the package.
     * 
     * @return Volume in cubic centimeters
     */
    public long calculateVolume() {
        return (long) width * height * length;
    }
    
    /**
     * Checks if any dimension is bulky (>= 150cm).
     * 
     * @return true if any dimension is >= 150cm
     */
    public boolean hasBulkyDimension() {
        return width >= 150 || height >= 150 || length >= 150;
    }
    
    /**
     * Checks if the volume is bulky (>= 1,000,000 cm³).
     * 
     * @return true if volume >= 1,000,000 cm³
     */
    public boolean hasBulkyVolume() {
        return calculateVolume() >= 1_000_000;
    }
    
    /**
     * Checks if the package is heavy (>= 20kg).
     * 
     * @return true if mass >= 20,000 grams
     */
    public boolean isHeavy() {
        return mass >= 20_000;
    }
}

