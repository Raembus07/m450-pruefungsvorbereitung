package ch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class MonatsRechnungTest {

  @ParameterizedTest
  @CsvSource({
      "0.5, 0.0",
      "5.0, 10.0",
      "50.0, 20.0",
      "1.0, 0.0",
      "1.1, 2.0",
      "10.0, 20.0",
      "10.01, 20.0"
  })
  @DisplayName("Berechne Datenkosten – gültige Werte")
  void berechneDatenkostenTest(double verbrauchGb, double expectedFrancs) {
    // Arrange
    MonatsRechnung monatsRechnung = new MonatsRechnung();

    // Act
    double actualValue =
        monatsRechnung.berechneDatenkosten(verbrauchGb);

    // Assert
    assertEquals(expectedFrancs, actualValue);
  }

  @ParameterizedTest
  @CsvSource({
      "-1.0",
      "-0.001"
  })
  @DisplayName("Exception bei negativem Datenvolumen")
  void ifDataVolumeIsNegative(double value) {
    // Arrange
    MonatsRechnung monatsRechnung = new MonatsRechnung();

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> monatsRechnung.berechneDatenkosten(value)
    );
  }
}