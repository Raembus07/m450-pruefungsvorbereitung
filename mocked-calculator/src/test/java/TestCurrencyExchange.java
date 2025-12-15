import ch.calc.Calculator;
import ch.calc.expression.IExpressionEvaluator;
import ch.currency.ExchangeRateProvider;
import ch.calc.ICalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class TestCurrencyExchange {

  private ICalculator testee;

  @Mock
  IExpressionEvaluator evaluator;

  @BeforeEach
  void setUp() {
    testee = new Calculator(new ExchangeRateProvider(), evaluator);
  }

  @Test
  @DisplayName("convert 100 CHF to USD - returns 91 USD -> without stubbed exchange rate provider")
  void test1(){
    final var chf = 100d;
    final var usd = 91d;

    final var result = testee.convertChfToUsd(chf);

    Assertions.assertEquals(usd, result);
  }
}
