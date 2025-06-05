package ai.thoughtful.platform.factory;

import ai.thoughtful.platform.factory.model.Package;
import ai.thoughtful.platform.factory.model.PackageFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PackageClassification Tests")
class PackageClassificationTest {

    @Nested
    @DisplayName("Standard Package Tests")
    class StandardPackageTests {

        @ParameterizedTest
        @CsvSource({
                "50, 50, 50, 10000",   // Small package, light weight
                "100, 50, 30, 15000",  // Medium package, moderate weight
                "99, 99, 99, 19999"    // Just below all thresholds (volume = 970,299)
        })
        @DisplayName("Should classify as standard for various non-bulky, non-heavy packages")
        void shouldClassifyAsStandardForVariousPackages(int width, int height, int length, double mass) {
            // Given
            Package pkg = PackageFactory.make(width, height, length, mass);

            // When
            EnumSet<PackageClassification> classification = PackageClassification.classify(pkg);

            // Then
            assertTrue(classification.isEmpty());
        }
    }

    @Nested
    @DisplayName("Bulky Package Tests")
    class BulkyPackageTests {

        @Test
        @DisplayName("Should classify as bulky when volume is exactly at threshold")
        void shouldClassifyAsBulkyWhenVolumeIsExactlyAtThreshold() {
            // Given
            Package pkg = PackageFactory.make(100, 100, 100, 5000);

            // When
            EnumSet<PackageClassification> classification = PackageClassification.classify(pkg);

            // Then
            assertTrue(classification.contains(PackageClassification.BULKY));
            assertFalse(classification.contains(PackageClassification.HEAVY));
            assertEquals(1, classification.size());
        }

        @ParameterizedTest
        @CsvSource({
                "150, 10, 10, 5000",   // Height at threshold
                "10, 150, 10, 5000",   // Width at threshold
                "10, 10, 150, 5000"    // Length at threshold
        })
        @DisplayName("Should classify as bulky when any dimension is >= 150cm")
        void shouldClassifyAsBulkyWhenAnyDimensionExceedsThreshold(int width, int height, int length, double mass) {
            // Given
            Package pkg = PackageFactory.make(width, height, length, mass);

            // When
            EnumSet<PackageClassification> classification = PackageClassification.classify(pkg);

            // Then
            assertTrue(classification.contains(PackageClassification.BULKY));
        }
    }

    @Nested
    @DisplayName("Heavy Package Tests")
    class HeavyPackageTests {

        @Test
        @DisplayName("Should classify as heavy when mass is exactly at threshold")
        void shouldClassifyAsHeavyWhenMassIsExactlyAtThreshold() {
            // Given
            Package pkg = PackageFactory.make(10, 10, 10, 20000);

            // When
            EnumSet<PackageClassification> classification = PackageClassification.classify(pkg);

            // Then
            assertTrue(classification.contains(PackageClassification.HEAVY));
            assertFalse(classification.contains(PackageClassification.BULKY));
            assertEquals(1, classification.size());
        }
    }

}

