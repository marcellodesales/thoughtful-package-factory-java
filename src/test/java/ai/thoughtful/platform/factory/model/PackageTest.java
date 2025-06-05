package ai.thoughtful.platform.factory.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Package Tests")
class PackageTest {

    @Nested
    @DisplayName("Valid Construction Tests")
    class ValidConstructionTests {

        @Test
        @DisplayName("Should create package with valid dimension and mass")
        void shouldCreatePackageWithValidDimensionAndMass() {
            // Given
            PackageDimension dimension = new PackageDimension(10, 20, 30);
            double mass = 15.5;

            // When
            Package pkg = new Package(dimension, mass);

            // Then
            assertEquals(dimension, pkg.dimension());
            assertEquals(mass, pkg.mass());
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.1, 1.0, 10.5, 100.0, 1000.0, Double.MAX_VALUE})
        @DisplayName("Should create package with various valid masses")
        void shouldCreatePackageWithVariousValidMasses(double validMass) {
            // Given
            PackageDimension dimension = new PackageDimension(10, 10, 10);

            // When & Then
            assertDoesNotThrow(() -> new Package(dimension, validMass));
        }

        @Test
        @DisplayName("Should create package with minimum positive mass")
        void shouldCreatePackageWithMinimumPositiveMass() {
            // Given
            PackageDimension dimension = new PackageDimension(10, 10, 10);
            double minMass = Double.MIN_VALUE;

            // When & Then
            assertDoesNotThrow(() -> new Package(dimension, minMass));
        }

        @Test
        @DisplayName("Should create package with very small positive mass")
        void shouldCreatePackageWithVerySmallPositiveMass() {
            // Given
            PackageDimension dimension = new PackageDimension(10, 10, 10);
            double smallMass = 0.001;

            // When
            Package pkg = new Package(dimension, smallMass);

            // Then
            assertEquals(smallMass, pkg.mass());
        }
    }

    @Nested
    @DisplayName("Invalid Construction Tests")
    class InvalidConstructionTests {

        @ParameterizedTest
        @ValueSource(doubles = {0.0, -0.1, -1.0, -10.5, -100.0, Double.NEGATIVE_INFINITY})
        @DisplayName("Should throw exception when mass is invalid")
        void shouldThrowExceptionWhenMassIsInvalid(double invalidMass) {
            // Given
            PackageDimension dimension = new PackageDimension(10, 10, 10);

            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Package(dimension, invalidMass));
            assertEquals("Can't create package with invalid mass", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when mass is zero")
        void shouldThrowExceptionWhenMassIsZero() {
            // Given
            PackageDimension dimension = new PackageDimension(10, 10, 10);
            double zeroMass = 0.0;

            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Package(dimension, zeroMass));
            assertEquals("Can't create package with invalid mass", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when mass is NaN")
        void shouldThrowExceptionWhenMassIsNaN() {
            // Given
            PackageDimension dimension = new PackageDimension(10, 10, 10);
            double nanMass = Double.NaN;

            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Package(dimension, nanMass));
            assertEquals("Can't create package with invalid mass", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when dimension is null")
        void shouldThrowExceptionWhenDimensionIsNull() {
            // Given
            PackageDimension nullDimension = null;
            double validMass = 10.0;

            // When & Then
            assertThrows(NullPointerException.class,
                () -> new Package(nullDimension, validMass));
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should create light package")
        void shouldCreateLightPackage() {
            // Given
            PackageDimension smallDimension = new PackageDimension(5, 5, 5);
            double lightMass = 1.0; // 1 kg

            // When
            Package pkg = new Package(smallDimension, lightMass);

            // Then
            assertEquals(smallDimension, pkg.dimension());
            assertEquals(lightMass, pkg.mass());
            assertTrue(pkg.mass() < 20_000); // Less than 20kg (20,000g)
        }

        @Test
        @DisplayName("Should create heavy package")
        void shouldCreateHeavyPackage() {
            // Given
            PackageDimension dimension = new PackageDimension(10, 10, 10);
            double heavyMass = 25_000; // 25 kg in grams

            // When
            Package pkg = new Package(dimension, heavyMass);

            // Then
            assertEquals(dimension, pkg.dimension());
            assertEquals(heavyMass, pkg.mass());
            assertTrue(pkg.mass() >= 20_000); // 20kg or more
        }

        @Test
        @DisplayName("Should create package at heavy threshold")
        void shouldCreatePackageAtHeavyThreshold() {
            // Given
            PackageDimension dimension = new PackageDimension(10, 10, 10);
            double thresholdMass = 20_000; // Exactly 20 kg

            // When
            Package pkg = new Package(dimension, thresholdMass);

            // Then
            assertEquals(thresholdMass, pkg.mass());
        }

        @Test
        @DisplayName("Should create bulky package by volume")
        void shouldCreateBulkyPackageByVolume() {
            // Given - volume >= 1,000,000 cm³
            PackageDimension bulkyDimension = new PackageDimension(100, 100, 100); // 1,000,000 cm³
            double normalMass = 5_000; // 5 kg

            // When
            Package pkg = new Package(bulkyDimension, normalMass);

            // Then
            assertEquals(bulkyDimension, pkg.dimension());
            assertEquals(1_000_000, pkg.dimension().getVolume());
            assertTrue(pkg.dimension().getVolume() >= 1_000_000);
        }

        @Test
        @DisplayName("Should create bulky package by dimension")
        void shouldCreateBulkyPackageByDimension() {
            // Given - one dimension >= 150 cm
            PackageDimension bulkyDimension = new PackageDimension(150, 10, 10);
            double normalMass = 5_000; // 5 kg

            // When
            Package pkg = new Package(bulkyDimension, normalMass);

            // Then
            assertEquals(bulkyDimension, pkg.dimension());
            assertTrue(pkg.dimension().height() >= 150 || 
                      pkg.dimension().width() >= 150 || 
                      pkg.dimension().length() >= 150);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @ParameterizedTest
        @CsvSource({
                "149, 10, 10, 5000",      // Just below dimension threshold
                "150, 10, 10, 5000",      // At dimension threshold
                "151, 10, 10, 5000",      // Just above dimension threshold
                "10, 149, 10, 5000",      // Width below threshold
                "10, 150, 10, 5000",      // Width at threshold
                "10, 10, 149, 5000",      // Length below threshold
                "10, 10, 150, 5000"       // Length at threshold
        })
        @DisplayName("Should handle dimension threshold edge cases")
        void shouldHandleDimensionThresholdEdgeCases(int height, int width, int length, double mass) {
            // Given
            PackageDimension dimension = new PackageDimension(height, width, length);

            // When & Then
            assertDoesNotThrow(() -> new Package(dimension, mass));
        }

        @ParameterizedTest
        @CsvSource({
                "10, 10, 10, 19999",      // Just below mass threshold
                "10, 10, 10, 20000",      // At mass threshold
                "10, 10, 10, 20001"       // Just above mass threshold
        })
        @DisplayName("Should handle mass threshold edge cases")
        void shouldHandleMassThresholdEdgeCases(int height, int width, int length, double mass) {
            // Given
            PackageDimension dimension = new PackageDimension(height, width, length);

            // When & Then
            assertDoesNotThrow(() -> new Package(dimension, mass));
        }

        @Test
        @DisplayName("Should handle volume threshold edge case")
        void shouldHandleVolumeThresholdEdgeCase() {
            // Given - exactly at volume threshold
            PackageDimension dimension = new PackageDimension(100, 100, 100); // exactly 1,000,000
            double mass = 10_000;

            // When
            Package pkg = new Package(dimension, mass);

            // Then
            assertEquals(1_000_000, pkg.dimension().getVolume());
        }
    }

    @Nested
    @DisplayName("Record Behavior Tests")
    class RecordBehaviorTests {

        @Test
        @DisplayName("Should implement equals correctly")
        void shouldImplementEqualsCorrectly() {
            // Given
            PackageDimension dimension1 = new PackageDimension(10, 20, 30);
            PackageDimension dimension2 = new PackageDimension(10, 20, 30);
            PackageDimension dimension3 = new PackageDimension(10, 20, 31);
            
            Package package1 = new Package(dimension1, 15.5);
            Package package2 = new Package(dimension2, 15.5);
            Package package3 = new Package(dimension3, 15.5);
            Package package4 = new Package(dimension1, 16.0);

            // Then
            assertEquals(package1, package2);
            assertNotEquals(package1, package3);
            assertNotEquals(package1, package4);
            assertEquals(package1, package1);
        }

        @Test
        @DisplayName("Should implement hashCode correctly")
        void shouldImplementHashCodeCorrectly() {
            // Given
            PackageDimension dimension = new PackageDimension(10, 20, 30);
            Package package1 = new Package(dimension, 15.5);
            Package package2 = new Package(dimension, 15.5);

            // Then
            assertEquals(package1.hashCode(), package2.hashCode());
        }

        @Test
        @DisplayName("Should implement toString correctly")
        void shouldImplementToStringCorrectly() {
            // Given
            PackageDimension dimension = new PackageDimension(10, 20, 30);
            Package pkg = new Package(dimension, 15.5);

            // When
            String toString = pkg.toString();

            // Then
            assertTrue(toString.contains("Package"));
            assertTrue(toString.contains("dimension"));
            assertTrue(toString.contains("mass"));
            assertTrue(toString.contains("15.5"));
        }
    }
}

