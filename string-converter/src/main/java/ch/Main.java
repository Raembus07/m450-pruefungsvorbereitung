package ch;

public class Main {

  public static void main(String[] args) {

    IStringConverter stringConverter = new StringConverter();
    NumberConverter converter = new NumberConverter(stringConverter);

    System.out.println("Round up:                 12.5f  =>  " + converter.roundUp(12.5f));
    System.out.println("Round down:               16.8f  =>  " + converter.roundDown(16.8f));

    System.out.println("Round to 10^1:           136.2f  =>  " + converter.roundToPowerOfTen(136.2f, 1));
    System.out.println("Round to 10^2:           136.2f  =>  " + converter.roundToPowerOfTen(136.2f, 2));
    System.out.println("Round to 10^3:           136.2f  =>  " + converter.roundToPowerOfTen(136.2f, 3));

    System.out.println();
    System.out.println("Round to 10^1: siebenundzwanzig  =>  " +
            converter.roundToPowerOfTen("siebenundzwanzig", 1));
    System.out.println("Round to 10^2: siebenundzwanzig  =>  " +
            converter.roundToPowerOfTen("siebenundzwanzig", 2));
  }
}