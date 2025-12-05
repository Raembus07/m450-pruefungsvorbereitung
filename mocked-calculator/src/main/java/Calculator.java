package main.java;

public class Calculator implements ICalculator {

  public static final double MAX_VALUE = Math.pow(10, 12);

  public Calculator() {
  }

  @Override
  public double add(double left, double right) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public double subtract(double left, double right) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public double multiply(double left, double right) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public double divide(double left, double right) {
    final var division = left / right;

    if (division > MAX_VALUE) {
      throw new ArithmeticException("Result exceeds maximum value of 10 ^ 12");
    }

    return division;
  }

  @Override
  public double convertUsdToChf(double usd) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public double convertChfToUsd(double chf) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}