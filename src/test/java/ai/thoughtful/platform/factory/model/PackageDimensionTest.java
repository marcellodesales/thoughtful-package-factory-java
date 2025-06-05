package ai.thoughtful.platform.factory.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PackageDimension Tests")
class PackageDimensionTest {

    @Nested
    @DisplayName("Valid Construction Tests")
    class ValidConstructionTests {

        @Test
        @DisplayName("Should create package dimension with valid positive dimensions")
        void shouldCreatePackageDimensionWithValidDimensions() {
            // Given
            int height = 10;
            int width = 20;
            int length = 30;

            // When
            PackageDimension dimension = new PackageDimension(height, width, length);

            // Then
            assertEquals(height, dimension.height());
            assertEquals(width, dimension.width());
            assertEquals(length, dimension.length());
        }

        @ParameterizedTest
        @CsvSource({
                "1, 1, 1",
                "10, 20, 30",
                "100, 150, 200",
                "999, 999, 999",
        })
        @DisplayName("Should create package dimension with various valid dimensions")
        void shouldCreatePackageDimensionWithVariousValidDimensions(int height, int width, int length) {
            // When & Then
            assertDoesNotThrow(() -> new PackageDimension(height, width, length));
        }
    }

    @Nested
    @DisplayName("Invalid Construction Tests")
    class InvalidConstructionTests {

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -10, -100, Integer.MIN_VALUE})
        @DisplayName("Should throw exception when height is invalid")
        void shouldThrowExceptionWhenHeightIsInvalid(int invalidHeight) {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> new PackageDimension(invalidHeight, 10, 10));
            assertEquals("Can't create package with invalid dimensions", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -10, -100, Integer.MIN_VALUE})
        @DisplayName("Should throw exception when width is invalid")
        void shouldThrowExceptionWhenWidthIsInvalid(int invalidWidth) {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> new PackageDimension(10, invalidWidth, 10));
            assertEquals("Can't create package with invalid dimensions", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -10, -100, Integer.MIN_VALUE})
        @DisplayName("Should throw exception when length is invalid")
        void shouldThrowExceptionWhenLengthIsInvalid(int invalidLength) {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> new PackageDimension(10, 10, invalidLength));
            assertEquals("Can't create package with invalid dimensions", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when all dimensions are zero")
        void shouldThrowExceptionWhenAllDimensionsAreZero() {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> new PackageDimension(0, 0, 0));
            assertEquals("Can't create package with invalid dimensions", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when all dimensions are negative")
        void shouldThrowExceptionWhenAllDimensionsAreNegative() {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> new PackageDimension(-1, -2, -3));
            assertEquals("Can't create package with invalid dimensions", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Volume Calculation Tests")
    class VolumeCalculationTests {

        @Test
        @DisplayName("Should calculate correct volume for small dimensions")
        void shouldCalculateCorrectVolumeForSmallDimensions() {
            // Given
            PackageDimension dimension = new PackageDimension(5, 10, 15);

            // When
            int volume = dimension.getVolume();

            // Then
            assertEquals(750, volume); // 5 * 10 * 15 = 750
        }

        @Test
        @DisplayName("Should calculate correct volume for unit dimensions")
        void shouldCalculateCorrectVolumeForUnitDimensions() {
            // Given
            PackageDimension dimension = new PackageDimension(1, 1, 1);

            // When
            int volume = dimension.getVolume();

            // Then
            assertEquals(1, volume);
        }

        @ParameterizedTest
        @CsvSource({
                "10, 10, 10, 1000",
                "5, 20, 25, 2500", 
                "100, 100, 100, 1000000",
                "2, 3, 4, 24",
                "150, 150, 150, 3375000"
        })
        @DisplayName("Should calculate correct volume for various dimensions")
        void shouldCalculateCorrectVolumeForVariousDimensions(int height, int width, int length, int expectedVolume) {
            // Given
            PackageDimension dimension = new PackageDimension(height, width, length);

            // When
            int volume = dimension.getVolume();

            // Then
            assertEquals(expectedVolume, volume);
        }

        @Test
        @DisplayName("Should calculate volume at bulky threshold")
        void shouldCalculateVolumeAtBulkyThreshold() {
            // Given - dimensions that result in exactly 1,000,000 cm³
            PackageDimension dimension = new PackageDimension(100, 100, 100);

            // When
            int volume = dimension.getVolume();

            // Then
            assertEquals(1_000_000, volume);
        }

        @Test
        @DisplayName("Should handle large volume calculations")
        void shouldHandleLargeVolumeCalculations() {
            // Given
            PackageDimension dimension = new PackageDimension(200, 200, 200);

            // When
            int volume = dimension.getVolume();

            // Then
            assertEquals(8_000_000, volume);
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

            // Then
            assertEquals(dimension1, dimension2);
            assertNotEquals(dimension1, dimension3);
            assertEquals(dimension1, dimension1);
        }

        @Test
        @DisplayName("Should implement hashCode correctly")
        void shouldImplementHashCodeCorrectly() {
            // Given
            PackageDimension dimension1 = new PackageDimension(10, 20, 30);
            PackageDimension dimension2 = new PackageDimension(10, 20, 30);

            // Then
            assertEquals(dimension1.hashCode(), dimension2.hashCode());
        }

        @Test
        @DisplayName("Should implement toString correctly")
        void shouldImplementToStringCorrectly() {
            // Given
            PackageDimension dimension = new PackageDimension(10, 20, 30);

            // When
            String toString = dimension.toString();

            // Then
            assertTrue(toString.contains("PackageDimension"));
            assertTrue(toString.contains("height=10"));
            assertTrue(toString.contains("width=20"));
            assertTrue(toString.contains("length=30"));
        }
    }
}

