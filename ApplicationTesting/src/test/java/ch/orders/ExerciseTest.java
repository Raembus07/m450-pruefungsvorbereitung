package ch.orders;

import ch.util.BaseMockitoUnitTest;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ExerciseTest extends BaseMockitoUnitTest {

  public static final String CH = "CH";

  private OrderTotalCalculator testee;

  @Mock
  private ITaxService taxService;
  @Mock
  private IDiscountService discountService;
  @Mock
  private IShippingService shippingService;

  @BeforeEach
  void setUp() {
    testee = new OrderTotalCalculator(taxService, discountService, shippingService);

    // returns the same amount that came into the methode
    Mockito.when(discountService.calculateDiscountAmount(
            Mockito.any(Order.class),
            Mockito.any(BigDecimal.class
            ))).thenReturn(BigDecimal.ZERO);
    Mockito.when(shippingService.getShippingCost(
            Mockito.any(Order.class),
            Mockito.any(BigDecimal.class)
    )).thenReturn(BigDecimal.ZERO);
    Mockito.when(taxService.getTaxRate(Mockito.any(String.class))).thenReturn(BigDecimal.ZERO);
  }

  @Nested
  class ExerciseTests {

    @Test
    @DisplayName("order with 1 line (no discount, no shipping fees, no tax) => total = line total")
    void test1() {
      final var unitPrice = BigDecimal.valueOf(100);
      final var order = createOrder(1, unitPrice);

      final var result = testee.calculateTotal(order);

      Assertions.assertEquals(unitPrice.intValue(), result.intValue());
    }

    @Test
    @DisplayName("order with 1 line - verifies that all services are called exactly once in correct order")
    void test2() {
      final var unitPrice = BigDecimal.valueOf(100);
      final var order = createOrder(1, unitPrice);

      testee.calculateTotal(order);

      final var inOrder = Mockito.inOrder(discountService, shippingService, taxService);

      inOrder.verify(discountService, Mockito.times(1))
              .calculateDiscountAmount(order, unitPrice);
      inOrder.verify(shippingService, Mockito.times(1))
              .getShippingCost(order, unitPrice);
      inOrder.verify(taxService, Mockito.times(1))
              .getTaxRate("CH");
      inOrder.verifyNoMoreInteractions();
    }

    @ParameterizedTest(
            name = "qty={0}, unitPrice={1} => subtotal={2}"
    )
    @CsvSource({
            "1, 10.00, 10.00",
            "2, 10.00, 20.00",
            "3, 5.50, 16.50",
            "10, 0.99, 9.90"
    })
    @DisplayName("4 orders with 1 line each - verifies subtotal calculation -> parametrized test")
    void test3(final int quantity,
               @Nonnull final String unitPriceStr,
               @Nonnull final String expectedSubtotalStr) {
      final var unitPrice = new BigDecimal(unitPriceStr);
      final var expectedSubtotal = new BigDecimal(expectedSubtotalStr);

      final var orderLines = new ArrayList<OrderLine>();
      orderLines.add(new OrderLine("A", quantity, unitPrice));
      final var order = new Order(orderLines, "CH", null);

      final var result = testee.calculateTotal(order);

      Assertions.assertEquals(0, expectedSubtotal.compareTo(result));
    }

    /*
     * Exercise 4 – Defensive Programming / Fail-Fast
     *
     * Aufgabenstellung:
     * - Stelle sicher, dass der OrderTotalCalculator bei ungültigen Eingaben sofort eine Exception wirft (fail-fast).
     * - In diesen Fällen dürfen KEINE externen Services aufgerufen werden (IDiscountService, IShippingService, ITaxService).
     * - Pro Äquivalenzklasse bzw. Grenzwert reicht ein repräsentativer Testfall.
     */

    @Test
    @DisplayName("order line without lines -> throws IllegalArgumentException")
    void test4() {
      final var order = new Order(new ArrayList<>(), "CH", null);

      assertIllegalArgumentThrown(order);
    }

    @Test
    @DisplayName("order line with negative quantity -> throws IllegalArgumentException")
    void test5() {
      final var order = createOrder(-1, BigDecimal.valueOf(100));

      assertIllegalArgumentThrown(order);
    }

    @Test
    @DisplayName("order line with negative unit price -> throws IllegalArgumentException")
    void test6() {
      final var order = createOrder(50, BigDecimal.valueOf(-10));

      assertIllegalArgumentThrown(order);
    }

    private void assertIllegalArgumentThrown(@Nonnull final Order order) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> testee.calculateTotal(order));
      Mockito.verify(taxService, Mockito.times(0)).getTaxRate(Mockito.anyString());
      Mockito.verify(discountService, Mockito.times(0)).calculateDiscountAmount(Mockito.any(Order.class), Mockito.any(BigDecimal.class));
      Mockito.verify(shippingService, Mockito.times(0)).getShippingCost(Mockito.any(Order.class), Mockito.any(BigDecimal.class));
    }

    @Test
    @DisplayName("order is null -> throws IllegalArgumentException and does NOT call services")
    void test7_orderIsNull_throws_andDoesNotCallServices() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> testee.calculateTotal(null));

      Mockito.verifyNoInteractions(taxService, discountService, shippingService);
    }

    @Test
    @DisplayName("order lines is null -> throws IllegalArgumentException and does NOT call services")
    void test8_linesIsNull_throws_andDoesNotCallServices() {
      final Order order = new Order(null, "CH", null);

      Assertions.assertThrows(IllegalArgumentException.class, () -> testee.calculateTotal(order));

      Mockito.verifyNoInteractions(taxService, discountService, shippingService);
    }

    @Test
    @DisplayName("country code is empty -> throws IllegalArgumentException and does NOT call services")
    void test9_countryCodeEmpty_throws_andDoesNotCallServices() {
      final var order = createOrder(1, new BigDecimal("10.00"));
      final var invalid = new Order(order.lines(), "", null);

      Assertions.assertThrows(IllegalArgumentException.class, () -> testee.calculateTotal(invalid));

      Mockito.verifyNoInteractions(taxService, discountService, shippingService);
    }

    @Test
    @DisplayName("country code is blank -> throws IllegalArgumentException and does NOT call services")
    void test10_countryCodeBlank_throws_andDoesNotCallServices() {
      final var order = createOrder(1, new BigDecimal("10.00"));
      final var invalid = new Order(order.lines(), "   ", null);

      Assertions.assertThrows(IllegalArgumentException.class, () -> testee.calculateTotal(invalid));

      Mockito.verifyNoInteractions(taxService, discountService, shippingService);
    }

    @Test
    @DisplayName("quantity is zero -> throws IllegalArgumentException and does NOT call services")
    void test11_quantityZero_throws_andDoesNotCallServices() {
      final var order = createOrder(0, new BigDecimal("10.00"));

      Assertions.assertThrows(IllegalArgumentException.class, () -> testee.calculateTotal(order));

      Mockito.verifyNoInteractions(taxService, discountService, shippingService);
    }

  }


  @Nested
  class RoundingTests {

    @Test
    @DisplayName("rounding from 10.03 to 10.05 (Swiss francs rounding rules)")
    void test1() {
      final var expected = BigDecimal.valueOf(10.05);
      final var unitPrice = BigDecimal.valueOf(10.03);
      final var order = createOrder(1, unitPrice);

      final var result = testee.calculateTotal(order);

      Assertions.assertEquals(expected, result);
    }

    @Test
    @DisplayName("rounding from 10.02 to 10.00 (Swiss francs rounding rules)")
    void test2() {
      final var expected = BigDecimal.valueOf(10.00);
      final var unitPrice = BigDecimal.valueOf(10.02);
      final var order = createOrder(1, unitPrice);

      final var result = testee.calculateTotal(order);

      Assertions.assertEquals(0, expected.compareTo(result));
    }

    @Test
    @DisplayName("rounding from 10.10 to 10.10 (Swiss francs rounding rules)")
    void test3() {
      final var unitPrice = BigDecimal.valueOf(10.10);
      final var order = createOrder(1, unitPrice);

      final var result = testee.calculateTotal(order);

      Assertions.assertEquals(0, unitPrice.compareTo(result));
    }

    @Test
    @DisplayName("rounding from 10.07 to 10.05 (Swiss francs rounding rules)")
    void test4() {
      final var unitPrice = BigDecimal.valueOf(10.07);
      final var expected = BigDecimal.valueOf(10.05);
      final var order = createOrder(1, unitPrice);

      final var result = testee.calculateTotal(order);

      Assertions.assertEquals(0, expected.compareTo(result));
    }

    @Test
    @DisplayName("rounding from 10.09 to 10.10 (Swiss francs rounding rules)")
    void test5() {
      final var unitPrice = BigDecimal.valueOf(10.09);
      final var expected = BigDecimal.valueOf(10.10);
      final var order = createOrder(1, unitPrice);

      final var result = testee.calculateTotal(order);

      Assertions.assertEquals(0, expected.compareTo(result));
    }

    @Test
    @DisplayName("rounding from 10.98 to 11.00 (Swiss francs rounding rules)")
    void test6() {
      final var expected = BigDecimal.valueOf(11.00);
      final var unitPrice = BigDecimal.valueOf(10.98);
      final var order = createOrder(1, unitPrice);

      final var result = testee.calculateTotal(order);

      Assertions.assertEquals(0, expected.compareTo(result));
    }

  }


  @Nonnull
  private Order createOrder(final int quantity,
                            @Nonnull final BigDecimal unitPrice) {
    final var orderLines = new ArrayList<OrderLine>();
    orderLines.add(new OrderLine("test", quantity, unitPrice));
    return new Order(orderLines, CH, null);
  }
}
