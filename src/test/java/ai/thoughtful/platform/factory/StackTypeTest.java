package ai.thoughtful.platform.factory;

import ai.thoughtful.platform.factory.algorithm.StackType;
import ai.thoughtful.platform.factory.model.Package;
import ai.thoughtful.platform.factory.model.PackageFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StackType Tests")
class StackTypeTest {

    @Nested
    @DisplayName("Standard Stack Tests")
    class StandardStackTests {

        @ParameterizedTest
        @CsvSource({
                "50, 50, 50, 10000",   // Small package, light weight
                "100, 50, 30, 15000",  // Medium package, moderate weight
                "99, 99, 99, 19999"    // Just below all thresholds (volume = 970,299)
        })
        @DisplayName("Should classify stack as STANDARD when package is neither bulky nor heavy")
        void shouldClassifyStackAsStandard(int width, int height, int length, double mass) {
            // When
            String result = StackType.sort(width, height, length, mass);

            // Then
            assertEquals(StackType.STANDARD.name(), result);
        }
    }

    @Nested
    @DisplayName("Special Stack Tests")
    class SpecialStackTests {

        @ParameterizedTest
        @CsvSource({
                "150, 10, 10, 5000",   // Bulky
                "10, 10, 10, 25000"    // Heavy
        })
        @DisplayName("Should classify stack as SPECIAL for bulky or heavy packages")
        void shouldClassifyStackAsSpecial(int width, int height, int length, double mass) {
            // When
            String result = StackType.sort(width, height, length, mass);

            // Then
            assertEquals(StackType.SPECIAL.name(), result);
        }
    }

    @Nested
    @DisplayName("Rejected Stack Tests")
    class RejectedStackTests {

        @Test
        @DisplayName("Should classify stack as REJECTED when package is both bulky and heavy")
        void shouldClassifyStackAsRejectedWhenBothBulkyAndHeavy() {
            // Given
            Package pkg = PackageFactory.builder()
                    .withHeightInCm(150)
                    .withWidthInCm(150)
                    .withLengthInCm(150)
                    .withMassInGrams(25000)
                    .build();

            // When
            StackType result = StackType.sort(pkg);

            // Then
            assertEquals(StackType.REJECTED, result);
        }
    }
}

