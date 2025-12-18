package ch.orders;

import ch.util.BaseMockitoUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Prüfungsvorbereitung – OrderTotalCalculator
 *
 * Zeigt:
 * - AAA-Muster
 * - Stubben mit Mockito
 * - verify / verifyNoInteractions
 * - InOrder (Reihenfolge)
 * - Exception-Test
 */
class Prüfungsvorbereitung extends BaseMockitoUnitTest {

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
}