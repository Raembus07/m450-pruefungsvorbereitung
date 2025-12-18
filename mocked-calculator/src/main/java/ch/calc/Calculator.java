package ch.calc;

import ch.calc.expression.IExpressionEvaluator;
import ch.currency.Currency;
import ch.currency.IExchangeRateProvider;
import jakarta.annotation.Nonnull;

public class Calculator implements ICalculator {

  public static final double MAX_VALUE = Math.pow(10, 12);
  @Nonnull
  final IExpressionEvaluator evaluator;

  @Nonnull
  final IExchangeRateProvider exchangeRateProvider;

  public Calculator(@Nonnull final IExchangeRateProvider exchangeRateProvider,
                    @Nonnull final IExpressionEvaluator evaluator) {
    this.exchangeRateProvider = exchangeRateProvider;
    this.evaluator = evaluator;
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
    return exchangeRateProvider.getRate(Currency.USD, Currency.CHF, usd);
  }

  @Override
  public double convertChfToUsd(double chf) {
    return exchangeRateProvider.getRate(Currency.CHF, Currency.USD, chf);
  }

  @Override
  public double eval(final String expression) {
    if (evaluator.expressionValidate(expression)) {
      return evaluator.expressionEvaluate(expression);
    } else {
      throw new IllegalArgumentException("Expression is not valid");
    }
  }

  @Override
  public double collectDataFrom(@Nonnull final IDataService dataService) {
    var sum = 0D;
    final var holder = new IntHolder();

    if (!dataService.opwen("data")) {
      return 0;
    }

    try {
      if (dataService.getFirst(holder)) {

        do {
          sum += holder.value;
        } while (dataService.getNext(holder));
      }
    } finally {
      dataService.close();
    }

    return sum;
  }
}