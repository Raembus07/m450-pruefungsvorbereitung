package ch.calc;

public interface ICalculator {

  double add(double left, double right);

  double subtract(double left, double right);

  double multiply(double left, double right);

  double divide(double left, double right) ;

  double convertUsdToChf(double usd);

  double convertChfToUsd(double chf);

  double eval(String expression);

  double collectDataFrom(IDataService dataService);
}