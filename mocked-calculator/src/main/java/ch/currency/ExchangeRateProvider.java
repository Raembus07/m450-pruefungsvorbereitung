package ch.currency;

import jakarta.annotation.Nonnull;

public class ExchangeRateProvider implements IExchangeRateProvider {

  @Override
  public double getRate(@Nonnull final Currency dst,
                        @Nonnull final Currency per,
                        final double amount) {
    if (dst == Currency.CHF && per == Currency.USD) {
      return amount * 0.91;
    } else if (dst == Currency.USD && per == Currency.CHF) {
      return amount * 1.10;
    } else {
      throw new IllegalArgumentException("Unsupported currency conversion");
    }
  }
}
