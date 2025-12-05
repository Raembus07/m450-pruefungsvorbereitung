package ch;

public class NumberConverter {

  private final IStringConverter stringConverter;

  public NumberConverter(IStringConverter stringConverter) {
    this.stringConverter = stringConverter;
  }

  public int roundUp(float value) {
    return 0; // wie im C#-Code: noch nicht implementiert
  }

  public int roundDown(float value) {
    return 0;
  }

  public int roundToPowerOfTen(float value, int precisionExponent) {
    return 0;
  }

  public int roundToPowerOfTen(String numericString, int precisionExponent) {
    return 0;
  }
}