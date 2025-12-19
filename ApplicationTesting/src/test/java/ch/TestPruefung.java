package ch.orders;

import ch.util.BaseMockitoUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Prüfungsvorbereitung – OrderTotalCalculator
 *
 * Zeigt:
 * - AAA-Muster
 * - Stubben mit Mockito
 * - verify / verifyNoInteractions
 * - InOrder (Reihenfolge)
 * - Exception-Test
 *
 * Zusätzlich (unten):
 * - Beispiele für Testdoubles: Dummy, Stub, Spy, Mock, Fake
 */
class TestPruefung extends BaseMockitoUnitTest {

  @Mock
  private ITaxService taxService;

  @Mock
  private IDiscountService discountService;

  @Mock
  private IShippingService shippingService;

  private OrderTotalCalculator testee;

  @BeforeEach
  void setUp() {
    testee = new OrderTotalCalculator(
        taxService,
        discountService,
        shippingService
    );
  }

  private static Order simpleOrder(BigDecimal price, int qty) {
    return new Order(
        List.of(new OrderLine("sku", qty, price)),
        "CH",
        null
    );
  }

  @Test
  void calculateTotal_simpleCase() {
    // Arrange
    Order order = simpleOrder(new BigDecimal("10.00"), 2); // subtotal 20.00

    when(discountService.calculateDiscountAmount(any(), any()))
        .thenReturn(BigDecimal.ZERO);
    when(shippingService.getShippingCost(any(), any()))
        .thenReturn(BigDecimal.ZERO);
    when(taxService.getTaxRate(anyString()))
        .thenReturn(BigDecimal.ZERO);

    // Act
    BigDecimal result = testee.calculateTotal(order);

    // Assert
    assertEquals(new BigDecimal("20.00"), result);

    verify(discountService).calculateDiscountAmount(any(), any());
    verify(shippingService).getShippingCost(any(), any());
    verify(taxService).getTaxRate("CH");
  }

  @Test
  void calculateTotal_invalidOrder_throwsException() {
    // Act + Assert
    assertThrows(IllegalArgumentException.class, () ->
        testee.calculateTotal(null)
    );

    verifyNoInteractions(
        discountService,
        shippingService,
        taxService
    );
  }

  @Test
  void calculateTotal_callsServicesInCorrectOrder() {
    // Arrange
    Order order = simpleOrder(new BigDecimal("5.00"), 2); // subtotal 10.00

    when(discountService.calculateDiscountAmount(any(), any()))
        .thenReturn(BigDecimal.ZERO);
    when(shippingService.getShippingCost(any(), any()))
        .thenReturn(BigDecimal.ZERO);
    when(taxService.getTaxRate(anyString()))
        .thenReturn(BigDecimal.ZERO);

    // Act
    testee.calculateTotal(order);

    // Assert
    InOrder inOrder = inOrder(
        discountService,
        shippingService,
        taxService
    );

    inOrder.verify(discountService)
        .calculateDiscountAmount(any(), any());
    inOrder.verify(shippingService)
        .getShippingCost(any(), any());
    inOrder.verify(taxService)
        .getTaxRate("CH");
  }

  /** Dummy: Platzhalter, wird nur übergeben (ohne Bedeutung für den Test). */
  static class DummyTaxService implements ITaxService {
    @Override
    public BigDecimal getTaxRate(String countryCode) {
      return BigDecimal.ZERO;
    }
  }

  /** Stub: liefert vordefinierte Rückgabewerte. */
  static class StubDiscountService implements IDiscountService {
    @Override
    public BigDecimal calculateDiscountAmount(Order order, BigDecimal subTotal) {
      return new BigDecimal("5.00");
    }
  }

  /** Spy: merkt sich, ob er aufgerufen wurde. */
  static class SpyShippingService implements IShippingService {
    boolean wasCalled = false;

    @Override
    public BigDecimal getShippingCost(Order order, BigDecimal subTotalAfterDiscount) {
      wasCalled = true;
      return BigDecimal.ZERO;
    }
  }

  /** Fake: vereinfachte echte Implementierung (nicht produktionsreif). */
  static class FakeTaxService implements ITaxService {
    @Override
    public BigDecimal getTaxRate(String countryCode) {
      return "CH".equals(countryCode)
          ? new BigDecimal("0.077")
          : BigDecimal.ZERO;
    }
  }

  @Test
  void dummy_example() {
    ITaxService dummy = new DummyTaxService();

    OrderTotalCalculator calc = new OrderTotalCalculator(
        dummy,
        discountService,
        shippingService
    );

    assertThrows(IllegalArgumentException.class, () -> calc.calculateTotal(null));
  }

  @Test
  void stub_example() {
    OrderTotalCalculator calc = new OrderTotalCalculator(
        taxService,
        new StubDiscountService(),
        shippingService
    );

    when(shippingService.getShippingCost(any(), any()))
        .thenReturn(BigDecimal.ZERO);
    when(taxService.getTaxRate(anyString()))
        .thenReturn(BigDecimal.ZERO);

    BigDecimal total = calc.calculateTotal(simpleOrder(new BigDecimal("10.00"), 1));
    assertEquals(new BigDecimal("5.00"), total);
  }

  @Test
  void spy_example() {
    SpyShippingService spy = new SpyShippingService();

    OrderTotalCalculator calc = new OrderTotalCalculator(
        taxService,
        discountService,
        spy
    );

    when(discountService.calculateDiscountAmount(any(), any()))
        .thenReturn(BigDecimal.ZERO);
    when(taxService.getTaxRate(anyString()))
        .thenReturn(BigDecimal.ZERO);

    calc.calculateTotal(simpleOrder(new BigDecimal("10.00"), 1));

    assertEquals(true, spy.wasCalled);
  }

  @Test
  void spy_with_mockito_example() {
    IShippingService real = new SpyShippingService();
    IShippingService spyShipping = spy(real);

    OrderTotalCalculator calc = new OrderTotalCalculator(
        taxService,
        discountService,
        spyShipping
    );

    when(discountService.calculateDiscountAmount(any(), any()))
        .thenReturn(BigDecimal.ZERO);
    when(taxService.getTaxRate(anyString()))
        .thenReturn(BigDecimal.ZERO);

    // optional: nur diese eine Methode "faken"/überschreiben
    doReturn(new BigDecimal("0.00"))
        .when(spyShipping)
        .getShippingCost(any(), any());

    // Act
    calc.calculateTotal(simpleOrder(new BigDecimal("10.00"), 1));

    // Assert: Aufruf verifizieren (das ist der eigentliche Spy-Usecase)
    verify(spyShipping).getShippingCost(any(), any());

    // (optional) falls du weiterhin dein Flag prüfen willst:
    // assertEquals(true, ((SpyShippingService) real).wasCalled);
  }

  @Test
  void mock_example() {
    Order order = simpleOrder(new BigDecimal("10.00"), 1);

    when(discountService.calculateDiscountAmount(any(), any()))
        .thenReturn(BigDecimal.ZERO);
    when(shippingService.getShippingCost(any(), any()))
        .thenReturn(BigDecimal.ZERO);
    when(taxService.getTaxRate(anyString()))
        .thenReturn(BigDecimal.ZERO);

    testee.calculateTotal(order);

    verify(discountService).calculateDiscountAmount(any(), any());
    verify(shippingService).getShippingCost(any(), any());
    verify(taxService).getTaxRate("CH");
  }

  @Test
  void fake_example() {
    OrderTotalCalculator calc = new OrderTotalCalculator(
        new FakeTaxService(),
        discountService,
        shippingService
    );

    when(discountService.calculateDiscountAmount(any(), any()))
        .thenReturn(BigDecimal.ZERO);
    when(shippingService.getShippingCost(any(), any()))
        .thenReturn(BigDecimal.ZERO);

    BigDecimal total = calc.calculateTotal(simpleOrder(new BigDecimal("10.00"), 1));
    assertEquals(new BigDecimal("10.75"), total);
  }
}