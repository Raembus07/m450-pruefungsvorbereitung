import ch.calc.Calculator;
import ch.calc.ICalculator;
import ch.calc.expression.IExpressionEvaluator;
import ch.currency.IExchangeRateProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class TestCalculator {

  private ICalculator testee;

  @Mock
  private IExchangeRateProvider exchangeRateProvider;
  @Mock
  IExpressionEvaluator evaluator;

  @BeforeEach
  void setUp(){
    testee = new Calculator(exchangeRateProvider, evaluator);
  }

  @Test
  @DisplayName("test division with valid values 24 & 12 - returns a non decimal number")
  void testDivision() {
    final var left = 24d;
    final var right = 12d;

    final var result = testee.divide(left, right);

    Assertions.assertEquals(2d, result);
  }

  @Test
  @DisplayName("test division with first value 0")
  void testDivision2(){
    final var left = 0d;
    final var right = 13d;

    final var result = testee.divide(left, right);

    Assertions.assertEquals(0, result);
  }

  @Test
  @DisplayName("test division with values 39 & 12 - returns a decimal number")
  void testDivision3(){
    final var left = 39d;
    final var right = 12d;

    final var result = testee.divide(left, right);

    Assertions.assertEquals(3.25, result);
  }

  @Test
  @DisplayName("test division with values 1000000000000d & 0.25 - throws ArithmeticException because result exceeds maximum value")
  void testDivision4(){
    final var left = 1000000000000d;
    final var right = 0.25d;

    Assertions.assertThrows(ArithmeticException.class, () -> testee.divide(left, right));
  }
}