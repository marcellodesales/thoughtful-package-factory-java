package ai.thoughtful.platform.factory.model;

/**
 * Fluent factory for creating Package instances with clear, self-documenting API.
 */
public class PackageFactory {

    /**
     * Factory method using PackageBuilder internally.
     * 
     * @param width box width in cm
     * @param height box height in cm
     * @param length box length in cm
     * @param mass box mass in grams
     * @return Package instance
     */
    public static Package make(int width, int height, int length, double mass) {
        return new PackageBuilder()
                .withWidthInCm(width)
                .withHeightInCm(height)
                .withLengthInCm(length)
                .withMassInGrams(mass)
                .build();
    }

    /**
     * Start building a package with fluent API.
     * 
     * @return PackageBuilder for fluent construction
     */
    public static PackageBuilder builder() {
        return new PackageBuilder();
    }

    /**
     * Start building package dimensions with fluent API.
     * 
     * @return DimensionBuilder for fluent construction
     */
    public static DimensionBuilder dimensionBuilder() {
        return new DimensionBuilder();
    }

    /**
     * Fluent builder for PackageDimension creation with validation.
     */
    public static class DimensionBuilder {
        private Integer lengthCm;
        private Integer widthCm;
        private Integer heightCm;

        private DimensionBuilder() {}

        /**
         * Set the length in centimeters.
         * 
         * @param lengthCm box length in centimeters
         * @return this builder for chaining
         */
        public DimensionBuilder withLengthInCm(int lengthCm) {
            this.lengthCm = lengthCm;
            return this;
        }

        /**
         * Set the width in centimeters.
         * 
         * @param widthCm box width in centimeters
         * @return this builder for chaining
         */
        public DimensionBuilder withWidthInCm(int widthCm) {
            this.widthCm = widthCm;
            return this;
        }

        /**
         * Set the height in centimeters.
         * 
         * @param heightCm box height in centimeters
         * @return this builder for chaining
         */
        public DimensionBuilder withHeightInCm(int heightCm) {
            this.heightCm = heightCm;
            return this;
        }

        /**
         * Build the PackageDimension instance after validating all required properties are set.
         * 
         * @return PackageDimension instance
         * @throws IllegalStateException if any required property is missing
         */
        public PackageDimension build() {
            if (lengthCm == null) {
                throw new IllegalStateException("Length must be set using withLengthInCm()");
            }
            if (widthCm == null) {
                throw new IllegalStateException("Width must be set using withWidthInCm()");
            }
            if (heightCm == null) {
                throw new IllegalStateException("Height must be set using withHeightInCm()");
            }

            return new PackageDimension(heightCm, widthCm, lengthCm);
        }
    }

    /**
     * Fluent builder for Package creation using composition with DimensionBuilder.
     */
    public static class PackageBuilder {
        private final DimensionBuilder dimensionBuilder;
        private Double massGrams;

        private PackageBuilder() {
            this.dimensionBuilder = new DimensionBuilder();
        }

        /**
         * Set the length in centimeters (proxy to DimensionBuilder).
         * 
         * @param lengthCm box length in centimeters
         * @return this builder for chaining
         */
        public PackageBuilder withLengthInCm(int lengthCm) {
            this.dimensionBuilder.withLengthInCm(lengthCm);
            return this;
        }

        /**
         * Set the width in centimeters (proxy to DimensionBuilder).
         * 
         * @param widthCm box width in centimeters
         * @return this builder for chaining
         */
        public PackageBuilder withWidthInCm(int widthCm) {
            this.dimensionBuilder.withWidthInCm(widthCm);
            return this;
        }

        /**
         * Set the height in centimeters (proxy to DimensionBuilder).
         * 
         * @param heightCm box height in centimeters
         * @return this builder for chaining
         */
        public PackageBuilder withHeightInCm(int heightCm) {
            this.dimensionBuilder.withHeightInCm(heightCm);
            return this;
        }

        /**
         * Set the mass in grams.
         * 
         * @param massGrams box mass in grams
         * @return this builder for chaining
         */
        public PackageBuilder withMassInGrams(double massGrams) {
            this.massGrams = massGrams;
            return this;
        }

        /**
         * Build the Package instance after validating all required properties are set.
         * Uses the composed DimensionBuilder to create the dimension.
         * 
         * @return Package instance
         * @throws IllegalStateException if any required property is missing
         */
        public Package build() {
            if (massGrams == null) {
                throw new IllegalStateException("Mass must be set using withMassInGrams()");
            }

            // Use the composed DimensionBuilder to create the dimension
            // This will validate all dimension properties internally
            PackageDimension dimension = dimensionBuilder.build();

            return new Package(dimension, massGrams);
        }
    }
}

