package ai.thoughtful.platform.factory.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PackageFactory Tests")
class PackageFactoryTest {

    @Nested
    @DisplayName("Valid Package Creation Tests")
    class ValidPackageCreationTests {

        @Test
        @DisplayName("Should create package with valid parameters")
        void shouldCreatePackageWithValidParameters() {
            // Given
            int width = 10;
            int height = 20;
            int length = 30;
            double mass = 15.5;

            // When
            Package pkg = PackageFactory.make(width, height, length, mass);

            // Then
            assertNotNull(pkg);
            assertEquals(height, pkg.dimension().height());
            assertEquals(width, pkg.dimension().width());
            assertEquals(length, pkg.dimension().length());
            assertEquals(mass, pkg.mass());
        }

        @Test
        @DisplayName("Should create package with minimum valid dimensions and mass")
        void shouldCreatePackageWithMinimumValidDimensionsAndMass() {
            // Given
            int width = 1;
            int height = 1;
            int length = 1;
            double mass = 0.1;

            // When
            Package pkg = PackageFactory.make(width, height, length, mass);

            // Then
            assertNotNull(pkg);
            assertEquals(height, pkg.dimension().height());
            assertEquals(width, pkg.dimension().width());
            assertEquals(length, pkg.dimension().length());
            assertEquals(mass, pkg.mass());
        }

        @ParameterizedTest
        @CsvSource({
                "10, 20, 30, 5.5",
                "1, 1, 1, 0.1",
                "100, 150, 200, 25000.0",
                "50, 75, 100, 15000.5",
                "150, 150, 150, 30000.0"
        })
        @DisplayName("Should create packages with various valid parameter combinations")
        void shouldCreatePackagesWithVariousValidParameterCombinations(int width, int height, int length, double mass) {
            // When
            Package pkg = PackageFactory.make(width, height, length, mass);

            // Then
            assertNotNull(pkg);
            assertEquals(height, pkg.dimension().height());
            assertEquals(width, pkg.dimension().width());
            assertEquals(length, pkg.dimension().length());
            assertEquals(mass, pkg.mass());
        }

        @Test
        @DisplayName("Should create package with parameter order mapping correctly")
        void shouldCreatePackageWithParameterOrderMappingCorrectly() {
            // Given - Note: PackageFactory.make(width, height, length, mass)
            // But PackageDimension(height, width, length)
            int factoryWidth = 10;
            int factoryHeight = 20;
            int factoryLength = 30;
            double mass = 15.5;

            // When
            Package pkg = PackageFactory.make(factoryWidth, factoryHeight, factoryLength, mass);

            // Then - Verify the parameter mapping
            assertEquals(factoryHeight, pkg.dimension().height());
            assertEquals(factoryWidth, pkg.dimension().width());
            assertEquals(factoryLength, pkg.dimension().length());
            assertEquals(mass, pkg.mass());
        }
    }

    @Nested
    @DisplayName("Invalid Package Creation Tests")
    class InvalidPackageCreationTests {

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -10, -100, Integer.MIN_VALUE})
        @DisplayName("Should throw exception when width is invalid")
        void shouldThrowExceptionWhenWidthIsInvalid(int invalidWidth) {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> PackageFactory.make(invalidWidth, 10, 10, 5.0));
            assertEquals("Can't create package with invalid dimensions", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -10, -100, Integer.MIN_VALUE})
        @DisplayName("Should throw exception when height is invalid")
        void shouldThrowExceptionWhenHeightIsInvalid(int invalidHeight) {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> PackageFactory.make(10, invalidHeight, 10, 5.0));
            assertEquals("Can't create package with invalid dimensions", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -10, -100, Integer.MIN_VALUE})
        @DisplayName("Should throw exception when length is invalid")
        void shouldThrowExceptionWhenLengthIsInvalid(int invalidLength) {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> PackageFactory.make(10, 10, invalidLength, 5.0));
            assertEquals("Can't create package with invalid dimensions", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.0, -0.1, -1.0, -100.0, Double.NEGATIVE_INFINITY})
        @DisplayName("Should throw exception when mass is invalid")
        void shouldThrowExceptionWhenMassIsInvalid(double invalidMass) {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> PackageFactory.make(10, 10, 10, invalidMass));
            assertEquals("Can't create package with invalid finite mass number", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when mass is NaN")
        void shouldThrowExceptionWhenMassIsNaN() {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> PackageFactory.make(10, 10, 10, Double.NaN));
            assertEquals("Can't create package with invalid finite mass number", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when all dimensions are invalid")
        void shouldThrowExceptionWhenAllDimensionsAreInvalid() {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> PackageFactory.make(0, 0, 0, 5.0));
            assertEquals("Can't create package with invalid dimensions", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should create standard package")
        void shouldCreateStandardPackage() {
            // Given - small dimensions and light weight
            int width = 10;
            int height = 10;
            int length = 10;
            double mass = 5000; // 5 kg

            // When
            Package pkg = PackageFactory.make(width, height, length, mass);

            // Then
            assertNotNull(pkg);
            assertTrue(pkg.dimension().getVolume() < 1_000_000); // Not bulky by volume
            assertTrue(pkg.dimension().height() < 150); // Not bulky by dimension
            assertTrue(pkg.dimension().width() < 150);
            assertTrue(pkg.dimension().length() < 150);
            assertTrue(pkg.mass() < 20_000); // Not heavy
        }

        @Test
        @DisplayName("Should create bulky package by volume")
        void shouldCreateBulkyPackageByVolume() {
            // Given - volume >= 1,000,000 cm³
            int width = 100;
            int height = 100;
            int length = 100;
            double mass = 5000; // 5 kg (not heavy)

            // When
            Package pkg = PackageFactory.make(width, height, length, mass);

            // Then
            assertNotNull(pkg);
            assertEquals(1_000_000, pkg.dimension().getVolume());
            assertTrue(pkg.dimension().getVolume() >= 1_000_000);
            assertTrue(pkg.mass() < 20_000); // Not heavy
        }

        @Test
        @DisplayName("Should create bulky package by dimension")
        void shouldCreateBulkyPackageByDimension() {
            // Given - one dimension >= 150 cm
            int width = 150;
            int height = 10;
            int length = 10;
            double mass = 5000; // 5 kg (not heavy)

            // When
            Package pkg = PackageFactory.make(width, height, length, mass);

            // Then
            assertNotNull(pkg);
            assertTrue(pkg.dimension().width() >= 150);
            assertTrue(pkg.dimension().getVolume() < 1_000_000); // Not bulky by volume
            assertTrue(pkg.mass() < 20_000); // Not heavy
        }

        @Test
        @DisplayName("Should create heavy package")
        void shouldCreateHeavyPackage() {
            // Given - mass >= 20 kg
            int width = 10;
            int height = 10;
            int length = 10;
            double mass = 25_000; // 25 kg

            // When
            Package pkg = PackageFactory.make(width, height, length, mass);

            // Then
            assertNotNull(pkg);
            assertTrue(pkg.mass() >= 20_000);
            assertTrue(pkg.dimension().getVolume() < 1_000_000); // Not bulky
            assertTrue(pkg.dimension().height() < 150);
            assertTrue(pkg.dimension().width() < 150);
            assertTrue(pkg.dimension().length() < 150);
        }

        @Test
        @DisplayName("Should create both bulky and heavy package")
        void shouldCreateBothBulkyAndHeavyPackage() {
            // Given - bulky by volume and heavy by mass
            int width = 100;
            int height = 100;
            int length = 100;
            double mass = 25_000; // 25 kg

            // When
            Package pkg = PackageFactory.make(width, height, length, mass);

            // Then
            assertNotNull(pkg);
            assertTrue(pkg.dimension().getVolume() >= 1_000_000); // Bulky
            assertTrue(pkg.mass() >= 20_000); // Heavy
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should create package at bulky volume threshold")
        void shouldCreatePackageAtBulkyVolumeThreshold() {
            // Given - exactly at volume threshold
            int width = 100;
            int height = 100;
            int length = 100; // exactly 1,000,000 cm³
            double mass = 10_000; // 10 kg

            // When
            Package pkg = PackageFactory.make(width, height, length, mass);

            // Then
            assertNotNull(pkg);
            assertEquals(1_000_000, pkg.dimension().getVolume());
        }

        @Test
        @DisplayName("Should create package at bulky dimension threshold")
        void shouldCreatePackageAtBulkyDimensionThreshold() {
            // Given - exactly at dimension threshold
            int width = 150;
            int height = 10;
            int length = 10;
            double mass = 10_000; // 10 kg

            // When
            Package pkg = PackageFactory.make(width, height, length, mass);

            // Then
            assertNotNull(pkg);
            assertEquals(150, pkg.dimension().width());
        }

        @Test
        @DisplayName("Should create package at heavy mass threshold")
        void shouldCreatePackageAtHeavyMassThreshold() {
            // Given - exactly at mass threshold
            int width = 10;
            int height = 10;
            int length = 10;
            double mass = 20_000; // exactly 20 kg

            // When
            Package pkg = PackageFactory.make(width, height, length, mass);

            // Then
            assertNotNull(pkg);
            assertEquals(20_000, pkg.mass());
        }

        @ParameterizedTest
        @CsvSource({
                "149, 10, 10, 19999",    // Just below both thresholds
                "150, 10, 10, 19999",    // At dimension threshold, below mass
                "149, 10, 10, 20000",    // Below dimension, at mass threshold
                "150, 10, 10, 20000",    // At both thresholds
                "151, 10, 10, 20001"     // Above both thresholds
        })
        @DisplayName("Should handle threshold edge cases")
        void shouldHandleThresholdEdgeCases(int width, int height, int length, double mass) {
            // When & Then
            assertDoesNotThrow(() -> PackageFactory.make(width, height, length, mass));
        }
    }

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("Should be a static factory method")
        void shouldBeAStaticFactoryMethod() {
            // Given
            int width = 10;
            int height = 20;
            int length = 30;
            double mass = 15.0;

            // When - calling static method
            Package pkg = PackageFactory.make(width, height, length, mass);

            // Then
            assertNotNull(pkg);
            assertInstanceOf(Package.class, pkg);
        }

        @Test
        @DisplayName("Should create different instances for same parameters")
        void shouldCreateDifferentInstancesForSameParameters() {
            // Given
            int width = 10;
            int height = 20;
            int length = 30;
            double mass = 15.0;

            // When
            Package pkg1 = PackageFactory.make(width, height, length, mass);
            Package pkg2 = PackageFactory.make(width, height, length, mass);

            // Then
            assertNotSame(pkg1, pkg2); // Different instances
            assertEquals(pkg1, pkg2);  // But equal content
        }

        @Test
        @DisplayName("Should maintain immutability of created packages")
        void shouldMaintainImmutabilityOfCreatedPackages() {
            // Given
            int width = 10;
            int height = 20;
            int length = 30;
            double mass = 15.0;

            // When
            Package pkg = PackageFactory.make(width, height, length, mass);

            // Then - Package is a record, so it's immutable
            assertNotNull(pkg.dimension());
            assertNotNull(pkg.mass());
            // No setter methods should exist (compile-time check)
        }
    }
}

