package ch.numberconverter;

import java.util.Arrays;

public class StringConverter implements IStringConverter {

  private static final String[] SINGLE_DIGIT_FRAGMENTS =
          {"_", "ein", "zwei", "drei", "vier", "fünf", "sechs", "sieben", "acht", "neun"};

  private static final String[] TEN_FRAGMENTS =
          {"_", "zehn", "zwanzig", "dreissig", "vierzig", "fünfzig", "sechzig", "siebzig", "achzig", "neunzig"};

  private static final String[] POSITIONAL_FRAGMENTS =
          {"_", "_", "hundert", "tausend"};

  /**
   * Converts a german numeric string into its numerical value.
   * This method currently only works with positive integer values <= 9999
   * and has certain flaws with irregular numeric strings (e.g. values 11, 12).
   */
  @Override
  public int convertToInt(String numericString) {
    int value = 0;

    for (int exponent = POSITIONAL_FRAGMENTS.length - 1; exponent > 1; exponent--) {
      if (numericString.contains(POSITIONAL_FRAGMENTS[exponent])) {
        String[] parts = numericString.split(POSITIONAL_FRAGMENTS[exponent], 2);
        int digit = Arrays.asList(SINGLE_DIGIT_FRAGMENTS).indexOf(parts[0]);
        value += digit * (int) Math.pow(10, exponent);
        numericString = parts.length > 1 ? parts[1] : "";
      }
    }

    for (int i = 1; i < TEN_FRAGMENTS.length; i++) {
      if (numericString.contains(TEN_FRAGMENTS[i])) {
        value += i * 10;
        numericString = numericString.replace(TEN_FRAGMENTS[i], "");
        break;
      }
    }

    for (int i = 1; i < SINGLE_DIGIT_FRAGMENTS.length; i++) {
      if (numericString.contains(SINGLE_DIGIT_FRAGMENTS[i])) {
        value += i;
        break;
      }
    }

    return value;
  }
}
