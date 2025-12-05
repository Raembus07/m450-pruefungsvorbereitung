public class StringConverter implements IStringConverter {

  private static final String[] SINGLE_DIGIT_FRAGMENTS =
          { "_", "ein", "zwei", "drei", "vier", "fünf", "sechs", "sieben", "acht", "neun" };

  private static final String[] TEN_FRAGMENTS =
          { "_", "zehn", "zwanzig", "dreissig", "vierzig", "fünfzig", "sechzig", "siebzig", "achzig", "neunzig" };

  private static final String[] POSITIONAL_FRAGMENTS =
          { "_", "_", "hundert", "tausend" };

  @Override
  public int convertToInt(String numericString) {
    int value = 0;

    for (int exponent = POSITIONAL_FRAGMENTS.length - 1; exponent > 1; exponent--) {
      if (numericString.contains(POSITIONAL_FRAGMENTS[exponent])) {
        String[] parts = numericString.split(POSITIONAL_FRAGMENTS[exponent]);
        value += indexOf(SINGLE_DIGIT_FRAGMENTS, parts[0]) * (int) Math.pow(10, exponent);
        numericString = parts[1];
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

  private int indexOf(String[] array, String val) {
    for (int i = 0; i < array.length; i++) {
      if (array[i].equals(val)) return i;
    }
    return -1;
  }
}