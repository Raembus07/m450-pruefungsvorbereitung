package ch.stub;

import ch.numberconverter.IStringConverter;
import jakarta.annotation.Nonnull;

public class StubStringConverter implements IStringConverter {

  @Override
  public int convertToInt(@Nonnull final String numericString) {
    if (numericString.contains("minus")) {
      throw new IllegalArgumentException("Negative numbers are not supported");
    }

    return switch (numericString) {
      case "siebenundzwanzig" -> 27;
      case "hundertdreiundzwanzig" -> 123;
      case "eintausendzweihundertdreiundvierzig" -> 1243;
      case "zweimilliardeneinhundertsiebenundvierzigmillionenvierhundertdreiundachtzigtausendsechshundertsiebenundvierzig" -> 2147483647;
      default -> 0;
    };
  }
}
