package main.java;

public interface IExchangeRateProvider {
  double getRate(Currency dst, Currency per);
}