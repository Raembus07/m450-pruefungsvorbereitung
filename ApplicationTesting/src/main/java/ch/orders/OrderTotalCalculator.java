package ch.orders;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class OrderTotalCalculator {

  public static final BigDecimal ROUNDING_STEP_SWISS_FRANCS = new BigDecimal("0.05");

  @Nonnull
  private final ITaxService taxService;
  @Nonnull
  private final IDiscountService discountService;
  @Nonnull
  private final IShippingService shippingService;

  public OrderTotalCalculator(@Nonnull final ITaxService taxService,
                              @Nonnull final IDiscountService discountService,
                              @Nonnull final IShippingService shippingService) {
    this.taxService = Objects.requireNonNull(taxService);
    this.discountService = Objects.requireNonNull(discountService);
    this.shippingService = Objects.requireNonNull(shippingService);
  }

  /**
   * subtotal = sum(quantity * unitPrice)
   * discount = capped to subtotal, must be >= 0
   * shipping = must be >= 0
   * taxRate = between 0 and 1
   * total = subtotalAfterDiscount + tax + shipping
   * rounding = 2 decimals, away from zero
   */
  @Nonnull
  public BigDecimal calculateTotal(@Nullable final Order order) {
    validateOrder(order);

    final var subTotal = calculatorSubtotal(order);
    final var afterDiscount = applyDiscount(order, subTotal);

    final var shipping = calculateShipping(order, afterDiscount);
    final var taxRate = calculateTaxRate(order);

    final var tax = afterDiscount.multiply(taxRate);
    final var total = afterDiscount.add(tax).add(shipping);

    return roundTotalBySwissRounding(total);
  }

  @Nonnull
  private BigDecimal calculateTaxRate(@Nonnull final Order order) {
    final var taxRate = taxService.getTaxRate(order.countryCode());
    if (taxRate.compareTo(BigDecimal.ZERO) < 0 || taxRate.compareTo(BigDecimal.ONE) > 0) {
      throw new IllegalArgumentException("TaxRate must be between 0 and 1");
    }

    return taxRate;
  }

  @Nonnull
  private BigDecimal calculateShipping(@Nonnull final Order order,
                                       @Nonnull final BigDecimal afterDiscount) {
    final var shipping = shippingService.getShippingCost(order, afterDiscount);
    if (shipping.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Shipping must be >= 0");
    }

    return shipping;
  }

  @Nonnull
  private BigDecimal applyDiscount(@Nonnull final Order order,
                                   @Nonnull final BigDecimal subTotal) {
    var discount = discountService.calculateDiscountAmount(order, subTotal);

    if (discount.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Discount must be >= 0");
    } else if (discount.compareTo(subTotal) > 0) {
      discount = subTotal;
    }

    return subTotal.subtract(discount);
  }

  private void validateOrder(@Nullable final Order order) {
    if (order == null) {
      throw new IllegalArgumentException("Order must not be null");
    }

    if (order.lines() == null || order.lines().isEmpty()) {
      throw new IllegalArgumentException("Order must contain at least one line");
    }

    if (order.countryCode() == null || order.countryCode().isBlank()) {
      throw new IllegalArgumentException("CountryCode must not be empty");
    }

    for (final var line : order.lines()) {
      if (line.quantity() <= 0) {
        throw new IllegalArgumentException("Quantity must be > 0");
      }
      if (line.unitPrice() == null || line.unitPrice().compareTo(BigDecimal.ZERO) < 0) {
        throw new IllegalArgumentException("UnitPrice must be >= 0");
      }
    }
  }

  @Nonnull
  private BigDecimal calculatorSubtotal(@Nonnull final Order order) {
    var subTotal = BigDecimal.ZERO;
    for (final var line : order.lines()) {
      subTotal = subTotal.add(line.unitPrice().multiply(BigDecimal.valueOf(line.quantity())));
    }
    return subTotal;
  }

  @Nonnull
  private BigDecimal roundTotalBySwissRounding(@Nonnull final BigDecimal amount) {
    return amount
        .divide(ROUNDING_STEP_SWISS_FRANCS, 0, RoundingMode.HALF_UP)
        .multiply(ROUNDING_STEP_SWISS_FRANCS)
        .setScale(2, RoundingMode.UNNECESSARY);
  }
}