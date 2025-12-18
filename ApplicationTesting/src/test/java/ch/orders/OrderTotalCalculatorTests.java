package ch.orders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderTotalCalculatorTests {

  private ITaxService tax;
  private IDiscountService discount;
  private IShippingService shipping;
  private OrderTotalCalculator sut;

  @BeforeEach
  void init() {
    tax = Mockito.mock(ITaxService.class, withSettings().strictness(org.mockito.quality.Strictness.STRICT_STUBS));
    discount = Mockito.mock(IDiscountService.class, withSettings().strictness(org.mockito.quality.Strictness.STRICT_STUBS));
    shipping = Mockito.mock(IShippingService.class, withSettings().strictness(org.mockito.quality.Strictness.STRICT_STUBS));

    sut = new OrderTotalCalculator(tax, discount, shipping);
  }

  @Test
  void calculateTotal_HappyPath_UsesAllServicesAndReturnsExpectedTotal() {
    // Arrange
    Order order = new Order(
            List.of(
                    new OrderLine("A", 2, new BigDecimal("10.00")), // 20
                    new OrderLine("B", 1, new BigDecimal("5.00"))   // 5 => subtotal 25
            ),
            "CH",
            "SAVE10"
    );

    when(discount.calculateDiscountAmount(order, new BigDecimal("25.00")))
            .thenReturn(new BigDecimal("5.00")); // after = 20
    when(shipping.getShippingCost(order, new BigDecimal("20.00")))
            .thenReturn(new BigDecimal("7.00"));
    when(tax.getTaxRate("CH"))
            .thenReturn(new BigDecimal("0.077")); // tax = 1.54

    // Act
    BigDecimal total = sut.calculateTotal(order);

    // Assert
    assertEquals(new BigDecimal("28.55"), total);

    verify(discount, times(1))
            .calculateDiscountAmount(order, new BigDecimal("25.00"));
    verify(shipping, times(1))
            .getShippingCost(order, new BigDecimal("20.00"));
    verify(tax, times(1))
            .getTaxRate("CH");

    verifyNoMoreInteractions(discount, shipping, tax);
  }

  @Test
  void calculateTotal_DiscountBiggerThanSubtotal_CapsDiscountToSubtotal() {
    // Arrange
    Order order = new Order(
            List.of(new OrderLine("A", 1, new BigDecimal("10.00"))),
            "CH",
            "FREE"
    );

    when(discount.calculateDiscountAmount(order, new BigDecimal("10.00")))
            .thenReturn(new BigDecimal("999.00")); // capped to 10 => after 0
    when(shipping.getShippingCost(order, new BigDecimal("0.00")))
            .thenReturn(new BigDecimal("6.00"));
    when(tax.getTaxRate("CH"))
            .thenReturn(new BigDecimal("0.077"));

    // Act
    BigDecimal total = sut.calculateTotal(order);

    // Assert
    assertEquals(new BigDecimal("6.00"), total);
  }

  @Test
  void calculateTotal_NegativeDiscount_Throws() {
    // Arrange
    Order order = new Order(
            List.of(new OrderLine("A", 1, new BigDecimal("10.00"))),
            "CH",
            "WEIRD"
    );

    when(discount.calculateDiscountAmount(order, new BigDecimal("10.00")))
            .thenReturn(new BigDecimal("-1.00"));

    // Act + Assert
    assertThrows(IllegalArgumentException.class, () -> sut.calculateTotal(order));
  }

  @Test
  void calculateTotal_NegativeShipping_Throws() {
    // Arrange
    Order order = new Order(
            List.of(new OrderLine("A", 1, new BigDecimal("10.00"))),
            "CH",
            null
    );

    when(discount.calculateDiscountAmount(order, new BigDecimal("10.00")))
            .thenReturn(new BigDecimal("0.00"));
    when(shipping.getShippingCost(order, new BigDecimal("10.00")))
            .thenReturn(new BigDecimal("-2.00"));

    // Act + Assert
    assertThrows(IllegalArgumentException.class, () -> sut.calculateTotal(order));
  }

  @Test
  void calculateTotal_InvalidTaxRate_Throws() {
    // Arrange
    Order order = new Order(
            List.of(new OrderLine("A", 1, new BigDecimal("10.00"))),
            "XX",
            null
    );

    when(discount.calculateDiscountAmount(order, new BigDecimal("10.00")))
            .thenReturn(new BigDecimal("0.00"));
    when(shipping.getShippingCost(order, new BigDecimal("10.00")))
            .thenReturn(new BigDecimal("0.00"));
    when(tax.getTaxRate("XX"))
            .thenReturn(new BigDecimal("1.5")); // invalid

    // Act + Assert
    assertThrows(IllegalArgumentException.class, () -> sut.calculateTotal(order));
  }

  @Test
  void calculateTotal_InvalidLineQuantity_Throws_AndDoesNotCallServices() {
    // Arrange
    Order order = new Order(
            List.of(new OrderLine("A", 0, new BigDecimal("10.00"))),
            "CH",
            null
    );

    // Act + Assert (fail fast)
    assertThrows(IllegalArgumentException.class, () -> sut.calculateTotal(order));

    verifyNoInteractions(discount, shipping, tax);
  }

  @Test
  void calculateTotal_RoundsToTwoDecimals_AwayFromZero() {
    // Arrange: tax = 0.005 -> rounds to 0.01
    Order order = new Order(
            List.of(new OrderLine("A", 1, new BigDecimal("10.00"))),
            "CH",
            null
    );

    when(discount.calculateDiscountAmount(order, new BigDecimal("10.00")))
            .thenReturn(new BigDecimal("0.00"));
    when(shipping.getShippingCost(order, new BigDecimal("10.00")))
            .thenReturn(new BigDecimal("0.00"));
    when(tax.getTaxRate("CH"))
            .thenReturn(new BigDecimal("0.0005"));

    // Act
    BigDecimal total = sut.calculateTotal(order);

    // Assert
    assertEquals(new BigDecimal("10.00"), total);
  }

}