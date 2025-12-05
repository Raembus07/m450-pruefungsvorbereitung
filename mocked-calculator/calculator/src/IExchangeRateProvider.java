public interface IExchangeRateProvider {
  double getRate(Currency dst, Currency per);
}