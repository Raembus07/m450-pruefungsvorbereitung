import ch.calc.Calculator;
import ch.calc.ICalculator;
import ch.calc.IDataService;
import ch.calc.IntHolder;
import ch.calc.expression.IExpressionEvaluator;
import ch.currency.IExchangeRateProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import util.BaseMockitoUnitTest;

public class TestDataService extends BaseMockitoUnitTest {

  private ICalculator testee;

  @Mock
  private IDataService dataService;
  @Mock
  private IExchangeRateProvider exchangeRateProvider;
  @Mock
  private IExpressionEvaluator evaluator;

  @BeforeEach
  void setUp() {
    testee = new Calculator(exchangeRateProvider, evaluator);

    Mockito.when(dataService.open(Mockito.anyString())).thenReturn(true);
    Mockito.doNothing().when(dataService).close();
    Mockito.when(dataService.getFirst(Mockito.any(IntHolder.class))).thenReturn(true);
    Mockito.when(dataService.getNext(Mockito.any(IntHolder.class))).thenReturn(true, true, false);
  }

  @Test
  void test1() {
    testee.collectDataFrom(dataService);

    Mockito.verify(dataService).open(Mockito.anyString());
    Mockito.verify(dataService).close();
    Mockito.verify(dataService, Mockito.times(1)).getFirst(Mockito.any(IntHolder.class));
    Mockito.verify(dataService, Mockito.times(3)).getNext(Mockito.any(IntHolder.class));
    Mockito.verifyNoMoreInteractions(dataService);
  }

}
