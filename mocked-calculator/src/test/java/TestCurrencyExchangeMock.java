import ch.calc.Calculator;
import ch.calc.expression.IExpressionEvaluator;
import ch.currency.Currency;
import ch.calc.ICalculator;
import ch.currency.IExchangeRateProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import util.BaseMockitoUnitTest;

public class TestCurrencyExchangeMock extends BaseMockitoUnitTest {

  private ICalculator testee;
  @Mock
  private IExchangeRateProvider exchangeRateProvider;
  @Mock
  IExpressionEvaluator evaluator;

  @BeforeEach
  void setUp() {
    Mockito.when(exchangeRateProvider.getRate(Mockito.any(Currency.class), Mockito.any(Currency.class), Mockito.anyDouble()))
            .thenReturn(200d);
    testee = new Calculator(exchangeRateProvider, evaluator);
  }

  @Test
  @DisplayName("test convertUsdToChf with 100 USD - returns 200 CHF")
  void testConvertChfToUsd() {
    final var chf = 100d;

    final var result = testee.convertChfToUsd(chf);

    final var usd = 200d;
    Assertions.assertEquals(usd, result);
    Mockito.verify(exchangeRateProvider, Mockito.times(0)).getRate(Currency.USD, Currency.CHF, chf);
    Mockito.verify(exchangeRateProvider, Mockito.times(1)).getRate(Currency.CHF, Currency.USD, chf);
    Mockito.verifyNoMoreInteractions(exchangeRateProvider);
  }

}
