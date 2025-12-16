import ch.calc.Calculator;
import ch.calc.ICalculator;
import ch.calc.expression.IExpressionEvaluator;
import ch.currency.IExchangeRateProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import util.BaseMockitoUnitTest;

public class TestExpressionEvaluator extends BaseMockitoUnitTest {

  public static final String VALID_EXPRESSION_REGEX = "^\\s*\\d+(\\.\\d+)?\\s*(\\+|\\-|\\*|\\/)\\s*\\d+(\\.\\d+)?$";
  public static final double EVALUATE_RESULT = 36.0;
  private ICalculator testee;
  @Mock
  private IExchangeRateProvider exchangeRateProvider;
  @Mock
  private IExpressionEvaluator evaluator;

  @BeforeEach
  void setUp() {
    Mockito.when(evaluator.expressionValidate(Mockito.matches(VALID_EXPRESSION_REGEX))).thenReturn(true);
    Mockito.when(evaluator.expressionEvaluate(Mockito.matches(VALID_EXPRESSION_REGEX))).thenReturn(EVALUATE_RESULT);

    testee = new Calculator(exchangeRateProvider, evaluator);
  }


  @Test
  @DisplayName("")
  void test1() {
    final var expression = "12 + 24";

    final var result = testee.eval(expression);

    Assertions.assertNotEquals("", result);
    Assertions.assertEquals(EVALUATE_RESULT, result);
    Mockito.verify(evaluator, Mockito.times(1)).expressionValidate(expression);
    Mockito.verify(evaluator, Mockito.times(1)).expressionEvaluate(expression);
    Mockito.verifyNoMoreInteractions(evaluator);
  }

  @Test
  void test2(){
    final var expression = "invalid expression";

    Mockito.when(evaluator.expressionValidate(Mockito.eq(expression))).thenReturn(false);

    Assertions.assertThrows(IllegalArgumentException.class, () -> testee.eval(expression));

    Mockito.verify(evaluator, Mockito.times(1)).expressionValidate(expression);
    Mockito.verify(evaluator, Mockito.times(0)).expressionEvaluate(expression);
    Mockito.verifyNoMoreInteractions(evaluator);
  }

}
