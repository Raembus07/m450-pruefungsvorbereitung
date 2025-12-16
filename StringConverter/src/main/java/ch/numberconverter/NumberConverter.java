package ch.numberconverter;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class NumberConverter {

  @Nullable
  private final IStringConverter stringConverter;

  public NumberConverter(@Nullable final IStringConverter stringConverter) {
    this.stringConverter = stringConverter;
  }

  public NumberConverter() {
    this(null);
  }

  public int roundUp(float value) {
    return (int) Math.ceil(value);
  }

  public int roundDown(float value) {
    return (int) Math.floor(value);
  }

  public int roundToPowerOfTen(float value, int precisionExponent) {
    if (precisionExponent < 0) {
      throw new IllegalArgumentException("precisionExponent must be >= 0");
    }

    double factor = Math.pow(10, precisionExponent);
    return (int) (Math.round(value / factor) * factor);
  }

  // Overload für Default-Parameter precisionExponent = 1
  public int roundToPowerOfTen(float value) {
    return roundToPowerOfTen(value, 1);
  }

  public int roundToPowerOfTen(@Nonnull final String numericString,
                               final int precisionExponent) {
    var number = 0;
    if (!numericString.isBlank()) {
      if (stringConverter != null) {
        number = stringConverter.convertToInt(numericString);
      }

      return roundToPowerOfTen((float) number, precisionExponent);
    }

    return number;
  }

  // Overload für Default-Parameter precisionExponent = 1
  public int roundToPowerOfTen(String numericString) {
    return roundToPowerOfTen(numericString, 1);
  }
}
