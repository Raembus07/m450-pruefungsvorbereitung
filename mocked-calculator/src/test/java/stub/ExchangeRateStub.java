package stub;

import ch.currency.Currency;
import ch.currency.IExchangeRateProvider;
import jakarta.annotation.Nonnull;

public class ExchangeRateStub implements IExchangeRateProvider {
  @Override
  public double getRate(@Nonnull final Currency dst,
                        @Nonnull final Currency per,
                        final double amount) {
    return 2;
  }
}
